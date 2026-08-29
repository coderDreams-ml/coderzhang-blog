// 文章详情页：?id=xxx 拉取并渲染 Markdown
async function fetchJSON(url) {
  const res = await fetch(url);
  if (!res.ok) throw new Error('http ' + res.status);
  const data = await res.json();
  if (data.code !== 0) throw new Error(data.message);
  return data.data;
}

document.addEventListener('DOMContentLoaded', async () => {
  const id = new URLSearchParams(location.search).get('id');
  if (!id) {
    document.getElementById('article-body').innerHTML = '<p class="meta">缺少文章 ID 参数</p>';
    return;
  }
  try {
    const a = await fetchJSON('/api/public/articles/' + id);
    document.title = a.title + ' · coderzhang.top';
    document.getElementById('article-title').textContent = a.title;
    const date = (a.createdAt || '').replace('T', ' ').slice(0, 16);
    const tags = (a.tags || []).map(t => '#' + t).join(' ');
    document.getElementById('article-meta').textContent =
      date + ' · ' + (a.categoryName || '未分类') + ' · ' + a.views + ' 次阅读 ' + tags;
    const raw = a.content || '';
    if (window.marked) {
      document.getElementById('article-body').innerHTML = marked.parse(raw);
    } else {
      // marked CDN 加载失败时的兜底：显示原文
      const pre = document.createElement('pre');
      pre.textContent = raw;
      document.getElementById('article-body').appendChild(pre);
    }
  } catch (e) {
    document.getElementById('article-body').innerHTML =
      '<p class="meta">文章加载失败：' + e.message + '</p>';
  }
});
