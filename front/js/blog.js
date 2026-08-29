// 博客列表页：从 /api/public/articles 拉取并分页
const PAGE_SIZE = 5;
let currentPage = 1;

async function fetchJSON(url) {
  const res = await fetch(url);
  if (!res.ok) throw new Error('http ' + res.status);
  const data = await res.json();
  if (data.code !== 0) throw new Error(data.message);
  return data.data;
}

function renderPosts(list) {
  const box = document.getElementById('post-list');
  if (!box) return;
  if (!list || !list.length) {
    box.innerHTML = '<div class="card"><p>还没有文章，去后台写第一篇吧 ✍️</p></div>';
    return;
  }
  box.innerHTML = '';
  list.forEach(a => {
    const card = document.createElement('a');
    card.className = 'card post-card';
    card.href = '/blog/article.html?id=' + a.id;
    const h = document.createElement('h3');
    h.textContent = a.title;
    const p = document.createElement('p');
    p.textContent = a.summary || '';
    const meta = document.createElement('div');
    meta.className = 'meta mono';
    const date = (a.createdAt || '').replace('T', ' ').slice(0, 10);
    const tags = (a.tags || []).join(' · ');
    meta.textContent = date + ' · ' + (a.categoryName || '未分类') + (tags ? ' · ' + tags : '') + ' · 阅读全文 →';
    card.append(h, p, meta);
    box.appendChild(card);
  });
}

async function load(page) {
  currentPage = page;
  try {
    const d = await fetchJSON('/api/public/articles?page=' + page + '&size=' + PAGE_SIZE);
    renderPosts(d.list);
    document.getElementById('page-info').textContent =
      '第 ' + page + ' 页 · 共 ' + d.total + ' 篇';
    document.getElementById('btn-prev').style.visibility = page > 1 ? 'visible' : 'hidden';
    document.getElementById('btn-next').style.visibility = page * PAGE_SIZE < d.total ? 'visible' : 'hidden';
  } catch (e) {
    console.log('articles api unavailable, keep static content');
  }
}

document.addEventListener('DOMContentLoaded', () => {
  load(1);
});
