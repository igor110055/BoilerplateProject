import {AppBar,
        Toolbar,
        IconButton,
        Typography,
        Stack,
        Button,
    } from '@mui/material'
import  CatchingPokemonIcon  from '@mui/icons-material/CatchingPokemon'
import Link from 'next/link'
import { useSession,  signOut } from "next-auth/react"
import React,{useState,useContext} from 'react'
import {MenuContext} from "@/store/MenuStore";

export const MuiNavbar = () => {
    const { data: session,status } = useSession()
    const [anchorEl,setAnchorEl] = useState<null | HTMLElement>(null)
    const open=Boolean(anchorEl)

    const menuStoreContext = useContext(MenuContext)
    const {setOneMenu,oneMenuList } = menuStoreContext;



  
  return (
    <>
    <AppBar position="static" >
        <Toolbar>
                <IconButton size='large'  edge="start" color="inherit" arial-label="logo" >
                <CatchingPokemonIcon />
                </IconButton>
                <Typography variant="h6" component="div" sx={{flexGrow: 1}}>
                    GOAV
                </Typography>
                <Stack direction="row" spacing={2}>
                    {
                        !session && (
                            <>
                                <Button color="inherit">
                                    <Link href="/member/login">
                                    <a>Sign in</a>
                                    </Link>                                    
                                </Button>
                                <Button color="inherit">
                                <Link href="/member/join">
                                    <a>회원가입</a>
                                </Link>
                                </Button>
                            </>
                        )
                    }
                    {
                        session && (
                            <>   
                               {oneMenuList.map((menu, index) => (
                                        <Button color="inherit" key={index} onClick={() => setOneMenu(menu)}>
                                            <a>{menu.MENU_NM}</a>
                                        </Button>
                                ))}
                                <Button color="inherit">
                                    <Link href="/member/my_page">
                                    <a>{
                                        (()=>{
                                            const tmp = session.user as any
                                            return tmp.nick_nm
                                       })() /*즉시 실행함수 */
                                    }</a>
                                    </Link>
                                </Button>
                                <Button color="inherit" onClick={() => signOut()}>Sign out</Button>
                            </>
                            
                        )
                    }
                </Stack>
        </Toolbar>
    </AppBar>
    </>
  )
}