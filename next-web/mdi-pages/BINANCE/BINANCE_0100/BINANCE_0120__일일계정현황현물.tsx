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


export const BINANCE_0120__일일계정현황현물= () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm,inlineDialog,inlineDialogClose } = menuStoreContext;
    const childRef = useRef<GridHandler>(null);
    const childRefBalances = useRef<GridHandler>(null);
    
    useEffect(()=>{

    },[])

    const [rows,setRows] = useState([]);
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError } = methods;

  
    const columns:OptColumn[] = [ 
        {header: 'SEQ',name: 'SEQ',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
        ,{header: 'TYPE',name: 'TYPE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'UPDATE_TIME',name: 'UPDATE_TIME',width: 140,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'TOTAL_ASSET_OF_BTC',name: 'TOTAL_ASSET_OF_BTC',width: 180,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
    ];

    const columns_balances:OptColumn[] = [ 
        {header: 'SEQ',name: 'SEQ',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
        ,{header: 'ASSET',name: 'ASSET',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'FREE',name: 'FREE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'LOCKED',name: 'LOCKED',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
    ];

    const fnSearch= async (data: IFormInput) => {
        if(childRef.current){
            const data:any= await send("BR_BINANCE_WALLET_GET_SAPI_V1_DailyAccountSnapshotSPOT", {
                brRq: 'IN_PSET'
                ,brRs: 'OUT_RSET,OUT_RSET_DATA__BALANCES'
            });        
            childRef.current.setData(data.OUT_RSET);

            if(childRefBalances.current){
                childRefBalances.current.setData(data.OUT_RSET_DATA__BALANCES);
            }
        }
        
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
            </Stack>
            <TuiGrid columns={columns} ref={childRef}   />


            <TuiGrid columns={columns_balances} ref={childRefBalances}   />
            </>
    )
  }
