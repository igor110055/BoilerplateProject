import React, { createContext,useState,useEffect,useRef} from "react";
import {IMenu} from "@/components/MuiLeftMenu"
import { useSession} from "next-auth/react"
import send from '@/utils/send';
import * as FlexLayout from 'flexlayout-react';
//https://github.com/abhijithvijayan/react-minimal-side-navigation
//https://codesandbox.io/s/react-minimal-side-navigation-example-y299d
import PageIndex,{IConfig} from "@/mdi-pages/PageIndex";
//https://www.storyblok.com/tp/react-dynamic-component-from-json
//동적호출에 대한 설명
import ConfirmDialog,{ConfirmDialogHandler} from '@/form-components/ConfirmDialog';
import AlertDialog,{AlertDialogHandler} from '@/form-components/AlertDialog';
import InlineDialog,{InlineDialogHandler} from '@/form-components/InlineDialog';
import { Breakpoint } from "@mui/material";

export const getBlankRight =(str:string,len:number):string=> {
    var tmp ="";
    var maxlen=0;
    if(str.length<=len){
        maxlen=len;
    } else {
        maxlen=str.length;
    }
    console.log('len==>'+len);
    for(var i=0;i<maxlen;i++){
        if(str.length>i){
            //-1하고 같기때문에 이렇게 되어야한다.
            tmp=tmp+str[i];
            console.log('a'+tmp);
        } else {
            tmp=tmp+" ";                    
            console.log('b'+tmp);
        }
        console.log(tmp);
    }
    return tmp;
}

interface MenuInterface {
    oneMenu: IMenu|null;
    setOneMenu: React.Dispatch<React.SetStateAction<IMenu|null>>;
    oneMenuList: IMenu[];
    menuList:IMenu[];
    flRef:React.RefObject<FlexLayout.Layout>;
    messageAlert:(value:string,fn?:any)=>void;
    messageConfirm:(value:string,fn:any)=>void;
    inlineDialog:(value:JSX.Element,fn?:any,fullWidth?:boolean,maxWidth?:Breakpoint)=>void;
    inlineDialogClose:()=>void;
  }


  
var json : FlexLayout.IJsonModel = {
    global: {},
    borders: [],
    layout:{
        "type": "row",
        "weight": 10,
        "children": [
            /*
            {
                "type": "tabset",
                "weight": 50,
                "selected": 0,
                "children": [
                    {
                        "type": "tab",
                        "name": "FX",
                        "component":"button",
                    },
                    {
                        "type": "tab",
                        "name": "FI",
                        "component":"button",
                    }
                ]
            }
            */
        ]
    }
};

//https://github.com/nealus/FlexLayout_cra_example
const model = FlexLayout.Model.fromJson(json);
model.doAction(FlexLayout.Actions.updateModelAttributes({
    splitterSize:20,
    tabSetHeaderHeight:40,
    tabSetTabStripHeight:40,
    tabSetMinHeight:500
}));

const factory = (node: FlexLayout.TabNode) => {
    var component2 = node.getComponent();
    console.log(component2)
    let config:IConfig = {
        component:component2!,
        id:"1"
    }    
    return PageIndex(config);
    
}

export const MenuContext = createContext<MenuInterface>({} as MenuInterface)

const MenuStore = (props:any) =>{
    const { data: session,status } = useSession()
    const [oneMenu,setOneMenu] = useState<IMenu|null>(null)
    const [oneMenuList,setOneMenuList] = useState<IMenu[]>([])
    const [menuList,setMenuList] = useState<IMenu[]>([])
    const flRef =  useRef<FlexLayout.Layout>(null);

    const childAlertRef = useRef<AlertDialogHandler>(null);
    const childConfirmRef = useRef<ConfirmDialogHandler>(null);
    const childInlineRef = useRef<InlineDialogHandler>(null);

    const messageAlert=(value:string,fn?:any)=>{
        if(childAlertRef.current) {
             childAlertRef.current.setValue(value,fn);
        }
    }

    const messageConfirm=(value:string,fn:any)=>{
        if(childConfirmRef.current) {
            childConfirmRef.current.setValue(value,fn);
        }
    }

    
    
    const inlineDialog=(value:JSX.Element,fn?:any,fullWidth?:boolean,maxWidth?:Breakpoint)=>{
        if(childInlineRef.current) {
            childInlineRef.current.setValue(value,fn,fullWidth,maxWidth);
        }
    }
 
    const inlineDialogClose=()=>{
        if(childInlineRef.current) {
            childInlineRef.current.close();
        }
    }


    useEffect(()=>{
        if(session){        
            const result = send("BR_CM_MAIN_FIND_TREE_BY_USER", {"brRq":"SESSION", "brRs":"OUT_DATA"})
            result.then(
                result=>{
                    //console.log({result})
                    let ret = result as any
                    let one = ret.OUT_DATA.filter((value:IMenu):boolean => {
                    //console.log(value)
                    if(value.MENU_LVL==="0") {
                            return true
                    } else {
                            return false
                    }
                    })
                    setOneMenuList(one)
                    let all = ret.OUT_DATA.filter((value:IMenu):boolean => {
                        return true

                })
                setMenuList(all)
                },
                err=>{
                    //console.log({result})
                }
            
            );
        }
    },[session])
    return (
        <MenuContext.Provider value={{oneMenu,setOneMenu,oneMenuList,menuList,flRef,messageAlert,messageConfirm,inlineDialog,inlineDialogClose}}>
            {props.children}
            <AlertDialog  ref={childAlertRef}  />
            <ConfirmDialog ref={childConfirmRef}  />
            <InlineDialog  ref={childInlineRef}  />
        </MenuContext.Provider>
    )
}
export default MenuStore;

export {model,factory};