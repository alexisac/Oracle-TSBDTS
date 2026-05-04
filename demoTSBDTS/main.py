from sentence_transformers import SentenceTransformer
# incarca modelul
model = SentenceTransformer("all-MiniLM-L12-v2")
text = "Salut lume"
embedding = model.encode(text)
print("Text:", text)
print("Dimensiune embedding:", len(embedding))
print("Embedding:")
print(embedding)