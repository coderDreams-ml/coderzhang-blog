// 本地预览 front：零依赖静态服务器 + /api 反向代理到本地后端 8080
// 用法：node scripts/dev-front.js  →  http://localhost:8081
const http = require('http');
const fs = require('fs');
const path = require('path');

const ROOT = path.join(__dirname, '..', 'front');
const PORT = 8081;
const API_TARGET = { host: '127.0.0.1', port: 8080 };

const MIME = {
  '.html': 'text/html; charset=utf-8',
  '.js': 'text/javascript; charset=utf-8',
  '.css': 'text/css; charset=utf-8',
  '.json': 'application/json; charset=utf-8',
  '.png': 'image/png',
  '.jpg': 'image/jpeg',
  '.svg': 'image/svg+xml',
  '.ico': 'image/x-icon',
  '.woff2': 'font/woff2'
};

http.createServer((req, res) => {
  const urlPath = decodeURIComponent(new URL(req.url, 'http://localhost').pathname);

  // /api 转发到本地后端
  if (urlPath.startsWith('/api/')) {
    const proxy = http.request(
      { host: API_TARGET.host, port: API_TARGET.port, path: req.url, method: req.method, headers: req.headers },
      (pr) => {
        res.writeHead(pr.statusCode, pr.headers);
        pr.pipe(res);
      }
    );
    proxy.on('error', () => {
      res.writeHead(502, { 'Content-Type': 'text/plain; charset=utf-8' });
      res.end('后端未启动：请先启动 Spring Boot (localhost:8080)');
    });
    req.pipe(proxy);
    return;
  }

  // 静态文件，目录回退到 index.html
  let file = path.join(ROOT, urlPath === '/' ? 'index.html' : urlPath);
  try {
    if (!fs.existsSync(file) || fs.statSync(file).isDirectory()) file = path.join(ROOT, 'index.html');
  } catch (e) {
    file = path.join(ROOT, 'index.html');
  }
  const ext = path.extname(file);
  res.writeHead(200, { 'Content-Type': MIME[ext] || 'application/octet-stream' });
  fs.createReadStream(file).pipe(res);
}).listen(PORT, () => {
  console.log('front dev server running: http://localhost:' + PORT);
  console.log('/api requests proxied to http://localhost:' + API_TARGET.port);
});
