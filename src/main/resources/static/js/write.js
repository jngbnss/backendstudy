const form = document.getElementById("post-form");

form.addEventListener("submit", async (event) => {
  event.preventDefault();

  const body = {
    title: document.getElementById("title").value.trim(),
    author: document.getElementById("author").value.trim(),
    content: document.getElementById("content").value.trim(),
  };

  const res = await fetch("/api/posts", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });

  if (!res.ok) {
    const error = await res.json().catch(() => null);
    alert(error?.message ?? "등록에 실패했습니다.");
    return;
  }

  const post = await res.json();
  location.href = `/post-detail.html?id=${post.id}`;
});
