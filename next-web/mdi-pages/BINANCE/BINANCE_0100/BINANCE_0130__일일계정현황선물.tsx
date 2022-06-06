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


export const BINANCE_0130__일일계정현황선물= () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm,inlineDialog,inlineDialogClose } = menuStoreContext;
    const childRef = useRef<GridHandler>(null);
    const childRefDataAssets = useRef<GridHandler>(null);
    const childRefDataPosition = useRef<GridHandler>(null);
    
    useEffect(()=>{

    },[])

    const [rows,setRows] = useState([]);
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError } = methods;

  
    const columns:OptColumn[] = [ 
        {header: 'SEQ',name: 'SEQ',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
        ,{header: 'TYPE',name: 'TYPE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
    ];

    const columns_data_assets:OptColumn[] = [ 
        {header: 'SEQ',name: 'SEQ',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
        ,{header: 'ASSET',name: 'ASSET',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'MARGIN_BALANCE',name: 'MARGIN_BALANCE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'WALLET_BALANCE',name: 'WALLET_BALANCE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
    ];
    
    const columns_data_position:OptColumn[] = [ 
        {header: 'SEQ',name: 'SEQ',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
        ,{header: 'ENTRY_PRICE',name: 'ENTRY_PRICE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'MARK_PRICE',name: 'MARK_PRICE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'POSITION_AMT',name: 'POSITION_AMT',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'SYMBOL',name: 'SYMBOL',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'UN_REALIZED_PROFIT',name: 'UN_REALIZED_PROFIT',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
    ];
    const fnSearch= async (data2: IFormInput) => {
        
            send("BR_BINANCE_WALLET_GET_SAPI_V1_DailyAccountSnapshotFUTURES", {
                brRq : 'IN_PSET'
                ,brRs : 'OUT_RSET,OUT_RSET_DATA__ASSETS,OUT_RSET_DATA__POSITION'
            }).then(function(data:any){
                if(childRef.current){
                    childRef.current.setData(data.OUT_RSET);
                    if(childRefDataAssets.current){
                        childRefDataAssets.current.setData(data.OUT_RSET_DATA__ASSETS);
                    }

                    if(childRefDataPosition.current){
                        childRefDataPosition.current.setData(data.OUT_RSET_DATA__POSITION);
                    }
                }
            }).catch(function(e){
                console.log({e})
                messageAlert(e.err_msg)

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
            </Stack>
            <TuiGrid columns={columns} ref={childRef}   />


            <TuiGrid columns={columns_data_assets} ref={childRefDataAssets}   />

            <TuiGrid columns={columns_data_position} ref={childRefDataPosition}   />
            </>
    )
  }
