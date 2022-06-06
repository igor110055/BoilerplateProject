import {Drawer,Box} from '@mui/material';
import Divider from '@mui/material/Divider';
import Toolbar from '@mui/material/Toolbar';
import React, { useState,useEffect,useContext } from "react";
import { useSession, signIn, signOut } from "next-auth/react"
import {Navigation,NavItemProps} from 'react-minimal-side-navigation';
import 'react-minimal-side-navigation/lib/ReactMinimalSideNavigation.css';
import {MenuContext,model,factory} from "@/store/MenuStore";
import * as pjtUtil from '@/utils/pjtUtil'
//https://github.com/abhijithvijayan/react-minimal-side-navigation
//메뉴 ui 에 대한 설명



export const drawerWidth = 200;
export interface IMenu {
    MENU_CD: string;
    MENU_CD_PATH: string;    
    MENU_KIND: string;
    MENU_LVL: string;
    MENU_NM: string;
    MENU_NO: string;
    MENU_PATH: string;
    ORD: string;
    PGM_ID: string;
    PRNT_MENU_CD: string;
    RMK: string;
}

export const MuiLeftMenu = () => {
    const { data: session,status } = useSession()
    const menuStoreContext = useContext(MenuContext)
    const {oneMenu,menuList,flRef } = menuStoreContext;
    const [secondMenu,setSecondMenu] = useState<NavItemProps[]>([])
    useEffect(()=>{
        //상단 메뉴를 클릭해서 menuStore가 바뀔때마다 다시 그려진다.
        //
        if(oneMenu){ 
            const dd= menuList.filter((value:IMenu):boolean =>  (value.PRNT_MENU_CD==oneMenu.MENU_CD)
                                    // 2dept메뉴 필터 
                                    ).map(m=>({
                                            title: m.MENU_NM,
                                            itemId: m.MENU_CD,
                                            subNav: menuList.filter((value:IMenu):boolean => (value.PRNT_MENU_CD==m.MENU_CD)
                                                                // 3dept메뉴 필터 
                                                                ).map(m2=>{
                                                                    const tmp = {
                                                                            title: m2.MENU_NM,
                                                                            itemId: m2.MENU_CD,
                                                                            subNav: Array<NavItemProps>()
                                                                        }
                                                                        //console.log(m2)
                                                                        return tmp;
                                                                    })
                                        }
                                    ))
            setSecondMenu(dd);
        }
    },[oneMenu]);

        
    const onAddActiveClick = (itemId:string) => {
        let tabId:string = "";
        for (let key in (model as any)._idMap) {
            const value = (model as any)._idMap[key];
            if(value._attributes.type==="tabset"){
                tabId=key;
            }            
        }

        const menu = menuList.filter((value:IMenu):boolean =>{
            if(value.MENU_CD==itemId) {
                return true;
            } else {
                return false;
            }
        })[0]
        console.log({menu})
        if(pjtUtil.isEmpty(menu.PGM_ID)){
            return ;
        }

        flRef.current!.addTabToTabSet(tabId,{
            component: menu.PGM_ID,
            name: menu.MENU_NM,
            config: {height:500}
        });
    }

     return (
            <>
            {
                oneMenu && 
                    <Box component="nav"
                        sx={{ width: { sm: drawerWidth }, flexShrink: { sm: 0 }  }}
                        aria-label="mailbox folders">
                    
                        <Drawer
                            variant="permanent"
                            sx={{
                            display: { xs: 'none', sm: 'block' },
                            '& .MuiDrawer-paper': { boxSizing: 'border-box', width: drawerWidth },
                            }}
                            open
                            >
                            <div>
                                <Toolbar />
                                <Divider />
                                <Navigation
                                            // you can use your own router's api to get pathname
                                            activeItemId="/management/members"
                                            onSelect={({itemId}) => {
                                                // maybe push to the route
                                                console.log(itemId);
                                                /*클릭한 것의 컴포넌트가 tab에 추가되면서 보이면 된다. */
                                                onAddActiveClick(itemId);
                                            }}
                                            items={secondMenu}
                                />
                            </div>

                        </Drawer>
                </Box>
            }
            </>
            )
}
