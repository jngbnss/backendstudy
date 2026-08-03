const listEl = document.getElementById("post-list");
const emptyEl = document.getElementById("empty");

async function loadPosts() {
  const res = await fetch("/api/posts");
  const posts = await res.json();

  if (posts.length === 0) {
    emptyEl.hidden = false;
    return;
  }

  listEl.innerHTML = posts.map(renderRow).join("");
}

function renderRow(post) {
  return `
    <tr onclick="location.href='/post-detail.html?id=${post.id}'">
      <td>${post.id}</td>
      <td class="title">${escapeHtml(post.title)}</td>
      <td>${escapeHtml(post.author)}</td>
      <td>${formatDate(post.createdAt)}</td>
    </tr>`;
}

function formatDate(iso) {
  if (!iso) return "";
  return iso.replace("T", " ").substring(0, 16);
}

// 제목/작성자에 <, > 같은 문자가 들어가도 안전하게 표시
function escapeHtml(text) {
  const div = document.createElement("div");
  div.textContent = text ?? "";
  return div.innerHTML;
}

loadPosts();
