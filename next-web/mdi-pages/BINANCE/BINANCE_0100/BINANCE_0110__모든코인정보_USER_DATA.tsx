import Grid,{GridHandler} from '@/form-components/Grid';
import { FormProvider, useForm } from "react-hook-form";
import {FormSelect} from '@/form-components/FormSelect';
import {FormTextFiled} from '@/form-components/FormTextFiled';
import React, { useState,useEffect,useRef, useContext } from "react";
import send from '@/utils/send';
import {Stack,Button,Typography,Breadcrumbs,Link} from '@mui/material'
import TuiGrid,{TuiGridHandler} from '@/form-components/TuiGrid';
import { CalculatedColumn } from 'react-data-grid';
import {MenuContext} from "@/store/MenuStore";
import { dark } from 'react-syntax-highlighter/dist/cjs/styles/prism';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { OptColumn } from 'tui-grid/types/options';

interface IFormInput {
}

const defaultValues = {
};


export const BINANCE_0110__모든코인정보_USER_DATA= () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm,inlineDialog,inlineDialogClose } = menuStoreContext;
    const childRef = useRef<GridHandler>(null);
    useEffect(()=>{

    },[])

    const [rows,setRows] = useState([]);
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError } = methods;

  
    const columns:OptColumn[] = [ 
        {header: 'COIN',name: 'COIN',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
        ,{header: 'DEPOSIT_ALL_ENABLE',name: 'DEPOSIT_ALL_ENABLE',width: 200,align : 'left',sortable : true,resizable: true,sortingType: 'desc'},{header: 'FREE',name: 'FREE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'FREEZE',name: 'FREEZE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'IPOABLE',name: 'IPOABLE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'IPOING',name: 'IPOING',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'IS_LEGAL_MONEY',name: 'IS_LEGAL_MONEY',width: 140,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'LOCKED',name: 'LOCKED',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'NAME',name: 'NAME',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'STORAGE',name: 'STORAGE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'TRADING',name: 'TRADING',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'WITHDRAW_ALL_ENABLE',name: 'WITHDRAW_ALL_ENABLE',width: 140,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'WITHDRAWING',name: 'WITHDRAWING',width: 140,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'NETWORK_CNT',name: 'NETWORK_CNT',width: 140,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
    ];


    const fnSearch= async (data: IFormInput) => {
        if(childRef.current){
            childRef.current.loadData({
                                        BR:"BR_BINANCE_WALLET_retrieveCoin",
                                        PARAM: {
                                            brRq : ''
                                            ,brRs : 'OUT_RSET'
                                        }
                                    });

        }
        
    }

    
    const syncHandler= async (data: IFormInput) => {

        messageConfirm("싱크하시겠습니까?",function()  {
            //_this.showProgress();	
            send('BR_BINANCE_WALLET_syncAllCoinsInformation',{
                brRq 		: ''
                ,brRs 		: ''
            }).then(function(data){
                //_this.hideProgress();
                if(data){
                    messageAlert("싱크되었습니다",function()  {
                        handleSubmit(fnSearch)()
                    });
                }
            })                      
        })
    }


    return (
            <>
            <Stack
                mt={2}
                direction="row"
                justifyContent="flex-start"
                alignItems="flex-start"
                spacing={0.5}
                >
                    <Button variant="contained" color="success"  onClick={handleSubmit(fnSearch)}>조회</Button>
                    <Button variant="contained" color="success" onClick={syncHandler}>싱크</Button>
            </Stack>
            <TuiGrid columns={columns} ref={childRef}   />
            </>
    )
  }
