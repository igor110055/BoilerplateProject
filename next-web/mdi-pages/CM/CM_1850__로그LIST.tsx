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

interface IFormInput {
}

const defaultValues = {
};


export const CM_1850__로그LIST = () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm,inlineDialog,inlineDialogClose } = menuStoreContext;
    const childRef = useRef<GridHandler>(null);
    useEffect(()=>{
        console.log("rrrr")
    },[])

    const [rows,setRows] = useState([]);
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError } = methods;
    const columns = [
        { key: 'BIZACTOR_LOG_ID', name: 'BIZACTOR_LOG_ID', width: 290 ,  sortable: true},
        { key: 'SEQ', name: 'SEQ', width: 80,  sortable: true , dataType: 'number'},
        { key: "ACT_ID", name: 'ACT_ID', width: 280 ,  sortable: true , resizable:true},
        { key: 'IN_DT_NAME', name: 'IN_DT_NAME', width: 200 ,  sortable: true, resizable:true},
        { key: 'OUT_DT_NAME', name: 'OUT_DT_NAME', width: 200 ,  sortable: true, resizable:true},
        { key: 'LOG_DATE', name: 'LOG_DATE', width: 200 , resizable:true},
        { key: 'ERR_STR', name: 'ERR_STR', width: 300 , resizable:true},
    ];


    const fnSearch= async () => {
        console.log("ddd");
        if(childRef.current){
            console.log("ddd1111");
            childRef.current.loadData({
                                        BR:"BR_CM_API_LOG_GetApiLogList",
                                        PARAM: {
                                            brRq : ''
                                            ,brRs : 'OUT_DATA'
                                        }
                                    });

        }
        
    }

    const fnDelete= async () => {
        console.log("fnDelete")
        send("BR_CM_API_LOG_DropLog", {
            brRq : ''
           ,brRs : ''
        }).then(function(data){
            //_this.hideProgress();
            if(data){
                messageAlert("삭제되었습니다",function()  {
                    fnSearch();
                });
            }
        })               
    }

    const gridOnRowClickHandler  = (row: any, column: CalculatedColumn<any, any>)=>{
        //BR_CM_API_LOG_GetApiLog
        console.log(row)
        inlineDialog(<CM_1850__로그LIST_detail bizactorLogId={row.BIZACTOR_LOG_ID}   />,undefined,false,"sm"); 
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
                    <Button variant="contained" color="success"  onClick={fnSearch}>조회</Button>
                    <Button variant="contained" color="success"  onClick={fnDelete}>삭제</Button>                    
            </Stack>
            <Grid columns={columns} checkbox={true} ref={childRef} onRowClick={gridOnRowClickHandler}   />
            </>
    )
  }
  

