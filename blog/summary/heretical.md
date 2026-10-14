## 有趣的地址收藏录




[ghs 小说](https://github.com/ltd0102/ghs)

[Bank Statement Templates](https://templatelab.com/bank-statement/#google_vignette)

[海外地址证明](https://addr.win/)


#### 科学上网
> 核心思路：在 VPS 服务端实现透明分流——连上该节点的客户端，访问 AI 相关（Anthropic/Claude 等）和 Stripe 支付相关的流量自动由 VPS 内部的 Cloudflare WARP 代理送出，其余流量正常走 VPS 原生机房出口。这样客户端无需配置复杂的本地分流规则，连上即走合规出口。
> 1. 准备一个美国 VPS 服务器：我用的是 DMIT（洛杉矶 CN2 线路），搬瓦工也可以，机房线路要有 CN2 等优化线路，要不然延迟和抖动太高。
> 2. 架构方案（Docker 一键编排）：在 VPS 上通过 Docker Compose 跑两个容器：
>  - WARP 容器：使用现成的 https://github.com/riba2534/warp-proxy 镜像，在内网提供一个本地 SOCKS5 代理（如 172.28.0.2:1080），经由此端口的所有流量全部自动转发至Cloudflare WARP。
>  - Mihomo 容器：使用官方 metacubex/mihomo 镜像作为服务端，对外提供 VLESS + Reality 节点。
