/** @type {import('next').NextConfig} */
// api  파일 크기 설정 https://nextjs.org/docs/api-routes/api-middlewares#custom-config
const nextConfig = {
  api: {
        bodyParser: true,
        responseLimit: false,
  },
  reactStrictMode: false,  /*이거 false로 해야함  이걸 true 로하면 useEffect가 2번 씩 실행됨 */
  redirects: async()=>{
      return [{
          source: '/about',
          destination: '/',
          permanent: true
      },{
          source: '/old-blog/:id',
          destination: '/new-blog/:id',
          permanent: true
      }
    
    ]
  }
}

module.exports = nextConfig
