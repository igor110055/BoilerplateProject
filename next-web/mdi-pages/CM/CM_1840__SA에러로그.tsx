import Grid,{GridHandler} from '@/form-components/Grid';
import { FormProvider, useForm } from "react-hook-form";
import {FormSelect} from '@/form-components/FormSelect';
import {FormTextFiled} from '@/form-components/FormTextFiled';
import React, { useState,useEffect,useRef, useContext } from "react";
import send from '@/utils/send';
import {Stack,Button,Typography,Breadcrumbs,Link} from '@mui/material'
import DataGrid, { Column,SelectColumn } from 'react-data-grid';
import { CalculatedColumn } from 'react-data-grid';
import { CM_1850__로그LIST_detail } from './CM_1850__로그LIST_detail';
import {MenuContext} from "@/store/MenuStore";
import { dark } from 'react-syntax-highlighter/dist/cjs/styles/prism';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';

interface IFormInput {
}

const defaultValues = {
};


export const CM_1840__SA에러로그 = () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm,inlineDialog,inlineDialogClose } = menuStoreContext;
    const childRef = useRef<GridHandler>(null);
    useEffect(()=>{
        console.log("rrrr")
    })

    const [rows,setRows] = useState([]);
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError } = methods;
    const columns = [
        { key: 'BR_NM', name: 'BR_NM', width: 200 ,  sortable: true},
        { key: 'SA_NM', name: 'SA_NM', width: 200,  sortable: true , dataType: 'number'},
        { key: "URL", name: 'URL', width: 140 ,  sortable: true , resizable:true},
        { key: 'JSON_OUT', name: 'JSON_OUT', width: 100 ,  sortable: true, resizable:true},
        { key: 'STATUS', name: 'STATUS', width: 80 ,  sortable: true, resizable:true},
        { key: 'ERR_CODE', name: 'ERR', width: 80 , resizable:true},
        { key: 'ERR_STACK_TRACE', name: 'ERR_STACK_TRACE', width: 200 , resizable:true},
        { key: 'QUERY_STRING', name: 'QUERY_STRING', width: 140 , resizable:true},
        { key: 'ERR_MSG', name: 'ERR_MSG', width: 300 , resizable:true},
        { key: 'CRT_DTM', name: '생성일', width: 160 },
    ];


    const fnSearch= async (data: IFormInput) => {
        if(childRef.current){
            childRef.current.loadData({
                                        BR:"BR_CM_API_LOG_retrieveBizActorSaErrLog",
                                        PARAM: {
                                            brRq : 'IN_DATA'
                                            ,brRs : 'OUT_DATA'
                                            ,IN_DATA:[data]
                                        }
                                    });

        }
        
    }

    const fnDelete= async (data: IFormInput) => {
        console.log("fnDelete")
        const result:any= await send("BR_CM_API_LOG_dropBizActorSaErrLog", {
            brRq : ''
           ,brRs : ''
        })
        handleSubmit(fnSearch)
    }

    const gridOnRowClickHandler  = (row: any, column: CalculatedColumn<any, any>)=>{
        //BR_CM_API_LOG_GetApiLog
        console.log(row)
        console.log(column)
        if(column.key=='BR_NM'
        ||column.key=='SA_NM'
        ||column.key=='URL'
        ||column.key=='JSON_OUT'
        ||column.key=='ERR_MSG'
        ||column.key=='ERR_STACK_TRACE'
        ||column.key=='QUERY_STRING'
        ){
            let tmp =row[column.key];
            console.log(tmp)
            const txt=(
                <SyntaxHighlighter language="plaintext" style={dark}>
                    {tmp}
                </SyntaxHighlighter>
            )
            inlineDialog(txt);
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
                    <Button variant="contained" color="success"  onClick={handleSubmit(fnDelete)}>삭제</Button>                    
            </Stack>
            <Grid columns={columns} checkbox={false} ref={childRef} onRowClick={gridOnRowClickHandler}   />
            </>
    )
  }
