const API = "/api/v1/posts";

const form = document.getElementById("post-form");
const titleInput = document.getElementById("title");
const writerInput = document.getElementById("writer");
const contentInput = document.getElementById("content");
const submitButton = document.getElementById("submit");
const composerAvatar = document.getElementById("composer-avatar");

const listEl = document.getElementById("post-list");
const emptyEl = document.getElementById("empty");
const feedEndEl = document.getElementById("feed-end");
const countEl = document.getElementById("count");
const messageEl = document.getElementById("message");
const writerListEl = document.getElementById("writer-list");

// 아바타 배경색 후보
const COLORS = ["#e1306c", "#f77737", "#fcaf45", "#405de6", "#5851db", "#833ab4", "#1abc9c", "#34495e"];

loadPosts();

// 닉네임 입력하면 글쓰기 아바타도 바뀐다
writerInput.addEventListener("input", function () {
  const name = writerInput.value.trim();
  paintAvatar(composerAvatar, name);
});

// "만들기" 누르면 제목 칸으로 이동
const composerNav = document.querySelector('[data-scroll="composer"]');
if (composerNav) {
  composerNav.addEventListener("click", function () {
    titleInput.focus();
    window.scrollTo({ top: 0, behavior: "smooth" });
  });
}

// 등록
form.addEventListener("submit", async function (event) {
  event.preventDefault(); // 폼 기본 전송(새로고침) 막기

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

    const keepWriter = body.writer;
    form.reset();
    writerInput.value = keepWriter; // 닉네임은 남겨둔다
    paintAvatar(composerAvatar, keepWriter);

    showMessage("올렸다", "success");
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
    renderWriters(posts);
  } catch (e) {
    showMessage("서버에 연결할 수 없다", "error");
  }
}

// 피드 그리기
function render(posts) {
  listEl.innerHTML = "";
  countEl.textContent = posts.length > 0 ? posts.length + "명" : "";

  if (posts.length === 0) {
    emptyEl.classList.remove("hidden");
    feedEndEl.classList.add("hidden");
    return;
  }

  emptyEl.classList.add("hidden");
  feedEndEl.classList.remove("hidden");

  // 최신 글이 위로 오게 뒤에서부터 돈다
  for (let i = posts.length - 1; i >= 0; i--) {
    listEl.appendChild(createPostCard(posts[i]));
  }
}

// 게시글 카드 하나
function createPostCard(post) {
  const article = document.createElement("article");
  article.className = "post";

  // 헤더: 아바타 + 닉네임 + 시간
  const head = document.createElement("div");
  head.className = "post-head";

  const ring = document.createElement("div");
  ring.className = "avatar-ring";
  const avatar = document.createElement("div");
  avatar.className = "avatar";
  paintAvatar(avatar, post.writer);
  ring.appendChild(avatar);

  const writer = document.createElement("span");
  writer.className = "post-writer";
  writer.textContent = post.writer;

  const time = document.createElement("span");
  time.className = "post-time";
  time.textContent = timeAgo(post.createdAt);
  time.title = formatDate(post.createdAt);

  const more = document.createElement("span");
  more.className = "post-more";
  more.textContent = "···";

  head.appendChild(ring);
  head.appendChild(writer);
  head.appendChild(time);
  head.appendChild(more);

  // 제목
  const title = document.createElement("h2");
  title.className = "post-title";
  title.textContent = post.title;

  // 내용
  const body = document.createElement("p");
  body.className = "post-body";
  body.textContent = post.content;

  // 액션 아이콘 (아직 기능 없음)
  const actions = document.createElement("div");
  actions.className = "post-actions";
  actions.innerHTML =
    '<svg viewBox="0 0 24 24"><path d="M16.792 3.904A4.989 4.989 0 0 1 21.5 9.122c0 3.072-2.652 4.959-5.197 7.222-2.512 2.243-3.865 3.469-4.303 3.752-.477-.309-2.143-1.823-4.303-3.752C5.152 14.081 2.5 12.194 2.5 9.122a4.989 4.989 0 0 1 4.708-5.218 4.21 4.21 0 0 1 3.675 1.941c.84 1.175.98 1.763 1.12 1.763s.278-.588 1.11-1.766a4.17 4.17 0 0 1 3.679-1.938Z"/></svg>' +
    '<svg viewBox="0 0 24 24"><path d="M20.656 17.008a9.993 9.993 0 1 0-3.59 3.615L22 22Z"/></svg>' +
    '<svg viewBox="0 0 24 24"><path d="M22 3 9.218 10.083M11.698 20.334 22 3.001 2 3l7.218 7.083 2.48 10.25Z"/></svg>' +
    '<span class="spacer"></span>' +
    '<svg viewBox="0 0 24 24"><path d="M20 21 12 13.44 4 21V3h16Z"/></svg>';

  // 번호
  const idEl = document.createElement("div");
  idEl.className = "post-id";
  idEl.textContent = "#" + post.id;

  article.appendChild(head);
  article.appendChild(title);
  article.appendChild(body);
  article.appendChild(actions);
  article.appendChild(idEl);
  return article;
}

// 우측 "최근 망한 사람" — 중복 닉네임 제거하고 최신순
function renderWriters(posts) {
  writerListEl.innerHTML = "";

  const seen = [];
  for (let i = posts.length - 1; i >= 0; i--) {
    const post = posts[i];
    if (seen.indexOf(post.writer) !== -1) {
      continue;
    }
    seen.push(post.writer);

    writerListEl.appendChild(createWriterItem(post));

    if (seen.length >= 5) {
      break;
    }
  }
}

function createWriterItem(post) {
  const li = document.createElement("li");
  li.className = "writer-item";

  const avatar = document.createElement("div");
  avatar.className = "avatar";
  paintAvatar(avatar, post.writer);

  const text = document.createElement("div");
  text.className = "writer-text";

  const name = document.createElement("span");
  name.className = "writer-name";
  name.textContent = post.writer;

  const sub = document.createElement("span");
  sub.className = "writer-sub";
  sub.textContent = post.title;

  text.appendChild(name);
  text.appendChild(sub);

  li.appendChild(avatar);
  li.appendChild(text);
  return li;
}

// 닉네임 첫 글자 + 이름으로 정한 색
function paintAvatar(element, name) {
  if (!name) {
    element.textContent = "?";
    element.style.background = "#737373";
    return;
  }

  element.textContent = name.substring(0, 1);

  let sum = 0;
  for (let i = 0; i < name.length; i++) {
    sum += name.charCodeAt(i);
  }
  element.style.background = COLORS[sum % COLORS.length];
}

// "방금", "5분", "3시간", "2일"
function timeAgo(value) {
  if (!value) {
    return "";
  }

  const created = new Date(value).getTime();
  const diff = Date.now() - created;

  const minute = 60 * 1000;
  const hour = 60 * minute;
  const day = 24 * hour;
  const week = 7 * day;

  if (diff < minute) {
    return "방금";
  }
  if (diff < hour) {
    return Math.floor(diff / minute) + "분";
  }
  if (diff < day) {
    return Math.floor(diff / hour) + "시간";
  }
  if (diff < week) {
    return Math.floor(diff / day) + "일";
  }
  return Math.floor(diff / week) + "주";
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
