import '@/styles/globals.css'
import type { AppProps } from 'next/app'
import App from 'next/app';
import { MuiNavbar } from '@/components/MuiNavbar';
import { SessionProvider } from "next-auth/react"
import {Box} from '@mui/material';
import {MuiLeftMenu} from '@/components/MuiLeftMenu';
import { createContext,  useState } from 'react';
import  MenuStore  from '@/store/MenuStore';
import { drawerWidth } from '@/components/MuiLeftMenu';



// UserDispatch 라는 이름으로 내보내줍니다.
export const MyStore = createContext(null);

function MyApp({ Component, pageProps: { session, ...pageProps } }: AppProps) {
    //console.log("aaa")
    //console.log(session)
    const [selOneDepth,setSelOneDept] = useState(null)
  return (
        <>  
            
                <SessionProvider session={session}>
                <MenuStore>
                <Box    sx={{ display: 'flex' }}>  
                    <MuiLeftMenu />
                    <Box    component="main"
                            sx={{ flexGrow: 1, p: 0, width: { sm: `calc(100% - ${drawerWidth}px)` } }}>
                        {
                            (Component.name!=="EmailCert" /* 이메일 인증 페이지는 MuiNavbar를 숨김 */)
                            && <MuiNavbar />
                        }
                        <Component {...pageProps} />
                    </Box>
                </Box>
                </MenuStore>
                </SessionProvider>
            
        </>) 
}

MyApp.getInitialProps = async (appContext:any) => {
    const { pageProps } = await App.getInitialProps(appContext);
    const { ctx } = appContext;
    return { pageProps};
};


export default MyApp
