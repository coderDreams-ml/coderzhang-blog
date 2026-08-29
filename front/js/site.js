// 前台主页动态渲染：从 /api/public 拉取数据，失败时保留页面内的静态内容
const API = '/api/public';

async function fetchJSON(url) {
  const res = await fetch(url);
  if (!res.ok) throw new Error('http ' + res.status);
  const data = await res.json();
  if (data.code !== 0) throw new Error(data.message);
  return data.data;
}

// 项目作品：按 groupName 分组渲染
async function renderProjects() {
  const grid = document.getElementById('project-grid');
  if (!grid) return;
  const section = grid.closest('#projects');
  try {
    const projects = await fetchJSON(API + '/projects');
    if (!projects || !projects.length) return;
    const groups = new Map();
    projects.forEach(p => {
      const g = p.groupName || '其他';
      if (!groups.has(g)) groups.set(g, []);
      groups.get(g).push(p);
    });
    const frag = document.createDocumentFragment();
    for (const [group, items] of groups) {
      const label = document.createElement('p');
      label.className = 'group-label';
      label.textContent = group;
      frag.appendChild(label);
      const wrap = document.createElement('div');
      wrap.className = 'grid grid-2';
      items.forEach(p => {
        const a = document.createElement('a');
        a.className = 'card';
        a.href = p.url || '#';
        a.target = '_blank';
        a.rel = 'noopener';
        const h = document.createElement('h3');
        h.textContent = p.name;
        const d = document.createElement('p');
        d.textContent = p.description || '';
        const meta = document.createElement('div');
        meta.className = 'meta';
        meta.textContent = (p.url || '').replace(/^https?:\/\//, '') + ' →';
        a.append(h, d, meta);
        wrap.appendChild(a);
      });
      frag.appendChild(wrap);
    }
    // 清空项目区除标题外的内容并插入
    section.querySelectorAll('.group-label, .grid, .meta').forEach(el => el.remove());
    section.appendChild(frag);
  } catch (e) {
    console.log('projects api unavailable, keep static content');
  }
}

// 社交链接
async function renderLinks() {
  const grid = document.getElementById('social-grid');
  if (!grid) return;
  try {
    const d = await fetchJSON(API + '/profile');
    const links = d.links || [];
    if (!links.length) return;
    grid.innerHTML = '';
    links.forEach(l => {
      const a = document.createElement('a');
      a.className = 'card social-card';
      a.href = l.url;
      a.target = '_blank';
      a.rel = 'noopener';
      const h = document.createElement('h3');
      h.textContent = l.platform;
      const p = document.createElement('p');
      p.textContent = l.url.replace(/^https?:\/\//, '');
      a.append(h, p);
      grid.appendChild(a);
    });
  } catch (e) {
    console.log('profile api unavailable, keep static content');
  }
}

// 关于我 / 技能栈 / 邮箱
async function renderProfile() {
  try {
    const d = await fetchJSON(API + '/profile');
    const s = d.settings || {};
    const introEl = document.getElementById('about-intro');
    if (introEl && s.intro) introEl.textContent = s.intro;
    const skillsEl = document.getElementById('skills-box');
    if (skillsEl && s.skills) {
      const items = s.skills.split(/[,，]/).map(x => x.trim()).filter(Boolean);
      if (items.length) {
        skillsEl.innerHTML = '';
        items.forEach(name => {
          const span = document.createElement('span');
          span.className = 'tag';
          span.textContent = name;
          skillsEl.appendChild(span);
        });
      }
    }
    const emailEl = document.getElementById('contact-email');
    if (emailEl && s.email) emailEl.textContent = '邮箱：' + s.email;
  } catch (e) {
    console.log('profile api unavailable');
  }
}

// 上报一次访问（IP 由后端哈希后落库，不存明文）
function reportVisit() {
  fetch(API + '/visit', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ path: location.pathname })
  }).catch(() => {});
}

// 渲染访客统计
async function renderVisitStats() {
  const uv = document.getElementById('visit-uv');
  const pv = document.getElementById('visit-pv');
  if (!uv && !pv) return;
  try {
    const d = await fetchJSON(API + '/stats');
    if (uv) uv.textContent = d.uv ?? '--';
    if (pv) pv.textContent = d.pv ?? '--';
  } catch (e) {
    console.log('stats api unavailable');
  }
}

document.addEventListener('DOMContentLoaded', () => {
  renderProjects();
  renderLinks();
  renderProfile();
  reportVisit();
  renderVisitStats();
});
