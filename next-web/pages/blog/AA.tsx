import type { NextPage } from 'next'
import { GetServerSideProps,GetStaticProps } from 'next'

const Blog: NextPage = () => {
    return (
      <div>블로그2222</div>
    )
  }
  
  export default Blog

  export const getServerSideProps: GetServerSideProps = async (context) => {
    console.log('가으자222')
    return { 
        props: {
            title:'ar'
        }
    }
  }
