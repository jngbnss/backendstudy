const postId = new URLSearchParams(location.search).get("id");

async function loadPost() {
  if (!postId) {
    alert("잘못된 접근입니다.");
    location.href = "/index.html";
    return;
  }

  const res = await fetch(`/api/posts/${postId}`);
  if (!res.ok) {
    alert("게시글을 찾을 수 없습니다.");
    location.href = "/index.html";
    return;
  }

  const post = await res.json();
  document.getElementById("title").textContent = post.title;
  document.getElementById("author").textContent = "작성자: " + post.author;
  document.getElementById("createdAt").textContent = formatDate(post.createdAt);
  document.getElementById("content").textContent = post.content;
  document.getElementById("edit-link").href = `/post-edit.html?id=${post.id}`;
}

document.getElementById("delete-btn").addEventListener("click", async () => {
  if (!confirm("정말 삭제하시겠습니까?")) return;

  const res = await fetch(`/api/posts/${postId}`, { method: "DELETE" });
  if (!res.ok) {
    alert("삭제에 실패했습니다.");
    return;
  }
  location.href = "/index.html";
});

function formatDate(iso) {
  if (!iso) return "";
  return iso.replace("T", " ").substring(0, 16);
}

loadPost();
