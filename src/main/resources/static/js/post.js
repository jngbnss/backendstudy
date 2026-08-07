const API = "/api/v1/posts";

const form = document.getElementById("post-form");
const titleInput = document.getElementById("title");
const writerInput = document.getElementById("writer");
const contentInput = document.getElementById("content");
const submitButton = form.querySelector("button");

const listEl = document.getElementById("post-list");
const emptyEl = document.getElementById("empty");
const countEl = document.getElementById("count");
const messageEl = document.getElementById("message");

// 페이지 열리면 목록 먼저 불러온다
loadPosts();

// 등록 버튼
form.addEventListener("submit", async function (event) {
  event.preventDefault(); // 기본 폼 전송(페이지 새로고침) 막기

  const body = {
    title: titleInput.value.trim(),
    writer: writerInput.value.trim(),
    content: contentInput.value.trim()
  };

  submitButton.disabled = true;
  showMessage("");

  try {
    const response = await fetch(API, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body)
    });

    if (!response.ok) {
      showMessage("등록 실패 (" + response.status + ")", "error");
      return;
    }

    form.reset();
    showMessage("등록 완료", "success");
    await loadPosts();
  } catch (e) {
    showMessage("서버에 연결할 수 없다", "error");
  } finally {
    submitButton.disabled = false;
  }
});

// 목록 조회
async function loadPosts() {
  try {
    const response = await fetch(API);
    if (!response.ok) {
      showMessage("목록 조회 실패 (" + response.status + ")", "error");
      return;
    }

    const posts = await response.json();
    render(posts);
  } catch (e) {
    showMessage("서버에 연결할 수 없다", "error");
  }
}

// 화면 그리기
function render(posts) {
  listEl.innerHTML = "";
  countEl.textContent = posts.length > 0 ? "(" + posts.length + ")" : "";

  if (posts.length === 0) {
    emptyEl.classList.remove("hidden");
    return;
  }
  emptyEl.classList.add("hidden");

  // 최신 글이 위로 오게 뒤에서부터 돈다
  for (let i = posts.length - 1; i >= 0; i--) {
    listEl.appendChild(createItem(posts[i]));
  }
}

function createItem(post) {
  const li = document.createElement("li");
  li.className = "post-item";

  const h3 = document.createElement("h3");
  h3.textContent = post.title;

  const meta = document.createElement("p");
  meta.className = "post-meta";
  meta.textContent = post.writer + " · " + formatDate(post.createdAt) + " · #" + post.id;

  const content = document.createElement("p");
  content.className = "post-content";
  content.textContent = post.content;

  li.appendChild(h3);
  li.appendChild(meta);
  li.appendChild(content);
  return li;
}

// "2026-08-07T15:04:33.123" -> "2026-08-07 15:04"
function formatDate(value) {
  if (!value) {
    return "";
  }
  return value.substring(0, 16).replace("T", " ");
}

function showMessage(text, type) {
  messageEl.textContent = text;
  messageEl.className = "message" + (type ? " " + type : "");
}
