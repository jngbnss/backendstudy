const postId = new URLSearchParams(location.search).get("id");
const form = document.getElementById("post-form");

document.getElementById("cancel-link").href = `/post-detail.html?id=${postId}`;

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
  document.getElementById("title").value = post.title;
  document.getElementById("author").value = post.author;
  document.getElementById("content").value = post.content;
}

form.addEventListener("submit", async (event) => {
  event.preventDefault();

  const body = {
    title: document.getElementById("title").value.trim(),
    author: document.getElementById("author").value.trim(),
    content: document.getElementById("content").value.trim(),
  };

  const res = await fetch(`/api/posts/${postId}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });

  if (!res.ok) {
    const error = await res.json().catch(() => null);
    alert(error?.message ?? "수정에 실패했습니다.");
    return;
  }

  location.href = `/post-detail.html?id=${postId}`;
});

loadPost();
