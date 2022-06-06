import type { NextPage } from 'next'
import { useEffect,useRef,useContext} from "react"
import { GetServerSideProps,GetStaticProps,InferGetServerSidePropsType } from 'next'
import send,{send_server} from '@/utils/send';
import { getSession } from 'next-auth/react'
import {MuiSpeedDial} from '@/components/MuiSpeedDial';
import * as FlexLayout from 'flexlayout-react';
import 'flexlayout-react/style/light.css';
import useAppBarHeight from '@/components/useAppBarHeight';
import {MenuContext,model,factory} from "@/store/MenuStore";
import { drawerWidth } from '@/components/MuiLeftMenu';

/* flexLayout 컨트롤 참고
https://codesandbox.io/s/flexlayout-react-6j9qy?file=/src/App.js:6120-6127
*/


const Home: NextPage = ({ articles }: InferGetServerSidePropsType<typeof getServerSideProps>) => {
    const menuStoreContext = useContext(MenuContext)
    const {flRef,oneMenu} = menuStoreContext;

    useEffect(()=>{
        const securePage = async ()=>{
            const session = await getSession()
            if(session) {
                //console.log("aaa")
                //console.log(session.user)
            }
        }
        securePage()
    },[])

    const appBarHeight = useAppBarHeight()
    console.log({appBarHeight})

    useEffect(()=>{
        const flexlayout =  document.getElementsByClassName("flexlayout__layout");
        const el =  flexlayout[0] as any ;
        el.style.marginTop=(appBarHeight)+"px"

        if(oneMenu!=null){
            console.log(drawerWidth)
            el.style.marginLeft=(drawerWidth)+"px"
        }
        //좌측 메뉴 보여질때랑 안보여질때도 이동이 되어야한다.
        //positon이 relative인게 문제인데...
        console.log(el)
        //el.style.position="relative" 이걸로 하면 사이즈가 줄어듬
        //el.style.setProperty("background-color", "red", "important");
    },[appBarHeight,oneMenu])
    
  return (
      <>
    <MuiSpeedDial />
    <FlexLayout.Layout
                ref={flRef}
              model={model}
              factory={factory} 
              onRenderTabSet={(tabSetNode, renderValues) => {
                  //console.log(tabSetNode);
              }
            }
        
              />
    </>
  )
}

export default Home
  
  export const getServerSideProps: GetServerSideProps = async (context) => {
     const data:any= await send_server("BR_TEST",{"brRq":"IN_PARAM", "brRs":"OUT_RESULT","IN_PARAM":[{"MSG":"TEST"}] },null)
     //console.log(data);
    
    return {
        props : {
            articles : data.OUT_RESULT[0].MSG          
        }
    }
  }
