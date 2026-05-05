import oracledb
from sentence_transformers import SentenceTransformer


DB_USER = "vector_user"
DB_PASSWORD = "vector_password"
DB_DSN = "localhost:1521/FREEPDB1"

MODEL_NAME = "all-MiniLM-L12-v2"
TOP_K = 2


def vector_to_oracle_literal(vector) -> str:
    """
    Transforma embedding-ul intr-un string acceptat de Oracle TO_VECTOR.
    Exemplu: [0.12345678,-0.23456789,...]
    """
    return "[" + ",".join(f"{float(value):.8f}" for value in vector) + "]"


def populate_missing_embeddings(connection, model):
    """
    Genereaza embeddings pentru metodele care incă au embedding NULL.
    """
    cursor = connection.cursor()

    select_sql = """
        SELECT id, symbol_name, source_code
        FROM code_chunks
        WHERE embedding IS NULL
        ORDER BY id
    """

    update_sql = """
        UPDATE code_chunks
        SET embedding = TO_VECTOR(:embedding)
        WHERE id = :id
    """

    try:
        cursor.execute(select_sql)
        rows = cursor.fetchall()

        print(f"Randuri fara embedding: {len(rows)}")

        for row_id, symbol_name, source_code in rows:
            if source_code is None:
                print(f"Sar peste ID {row_id}, source_code este NULL")
                continue

            source_text = str(source_code)

            print(f"Generez embedding pentru: {symbol_name}")

            embedding = model.encode(source_text)
            oracle_vector = vector_to_oracle_literal(embedding)

            cursor.execute(
                update_sql,
                {
                    "embedding": oracle_vector,
                    "id": row_id
                }
            )

        connection.commit()
        print("Embeddings salvate cu succes in DB.")

    except Exception:
        connection.rollback()
        raise

    finally:
        cursor.close()


def search_code(connection, model, question: str, top_k: int = TOP_K):
    """
    Genereaza embedding pentru intrebarea utilizatorului si cauta cele mai apropiate metode.
    """
    question_embedding = model.encode(question)
    question_vector = vector_to_oracle_literal(question_embedding)

    cursor = connection.cursor()

    search_sql = """
        SELECT
            id,
            file_path,
            class_name,
            symbol_name,
            chunk_type,
            ROUND(
                VECTOR_DISTANCE(embedding, TO_VECTOR(:query_vector), COSINE),
                4
            ) AS distance,
            DBMS_LOB.SUBSTR(source_code, 2000, 1) AS source_preview
        FROM code_chunks
        WHERE embedding IS NOT NULL
        ORDER BY VECTOR_DISTANCE(embedding, TO_VECTOR(:query_vector), COSINE)
        FETCH FIRST :top_k ROWS ONLY
    """

    try:
        cursor.execute(
            search_sql,
            {
                "query_vector": question_vector,
                "top_k": top_k
            }
        )

        results = cursor.fetchall()
        return results

    finally:
        cursor.close()


def print_results(results):
    if not results:
        print("Nu am gasit rezultate.")
        return

    print("\nRezultate gasite:")

    for row in results:
        row_id, file_path, class_name, symbol_name, chunk_type, distance, source_preview = row

        print("\n" + "-" * 80)
        print(f"ID: {row_id}")
        print(f"File: {file_path}")
        print(f"Class: {class_name}")
        print(f"Method: {symbol_name}")
        print(f"Type: {chunk_type}")
        print(f"Distance: {distance}")
        print("\nCode preview:")
        print(source_preview)


def main():
    print("Incarc modelul de embeddings...")
    model = SentenceTransformer(MODEL_NAME)

    print("Conectare la Oracle...")
    connection = oracledb.connect(
        user=DB_USER,
        password=DB_PASSWORD,
        dsn=DB_DSN
    )

    try:
        populate_missing_embeddings(connection, model)

        print("\nCautare semantica in cod")
        print("Scrie o intrebare sau 'exit' pentru oprire.")

        while True:
            question = input("\nIntrebare: ").strip()

            if question.lower() == "exit":
                break

            if not question:
                print("Introdu o intrebare valida.")
                continue

            results = search_code(connection, model, question, TOP_K)
            print_results(results)

    except Exception as error:
        print("A aparut o eroare:")
        raise error

    finally:
        connection.close()
        print("\nConexiunea la DB a fost inchisa.")


if __name__ == "__main__":
    main()