import Grid,{GridHandler} from '@/form-components/Grid';
import { FormProvider, useForm } from "react-hook-form";
import {FormSelect} from '@/form-components/FormSelect';
import {FormTextFiled} from '@/form-components/FormTextFiled';
import React, { useState,useEffect,useRef, useContext } from "react";
import send from '@/utils/send';
import {Stack,Button,Typography,Breadcrumbs,Link} from '@mui/material'
import TuiGrid,{TuiGridHandler} from '@/form-components/TuiGrid';
import {MenuContext} from "@/store/MenuStore";
import { OptColumn } from 'tui-grid/types/options';

interface IFormInput {
    COIN:string;
    NETWORK:string;
}

const defaultValues = {
    COIN:"",
    NETWORK:"",
};


export const BINANCE_0155__입금주소= () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm,inlineDialog,inlineDialogClose } = menuStoreContext;
    const childRef = useRef<GridHandler>(null);
    useEffect(()=>{

    },[])

    const [rows,setRows] = useState([]);
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError } = methods;

  
    const columns:OptColumn[] = [ 
        {header: 'ADDRESS',name: 'ADDRESS',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'COIN',name: 'COIN',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'TAG',name: 'TAG',width: 150,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'URL',name: 'URL',width: 150,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
    ];


    const fnSearch= async (data: IFormInput) => {
        if(childRef.current){
            childRef.current.loadData({
                                        BR:"BR_BINANCE_WALLET_GET_SAPI_V1_DepositAddress",
                                        PARAM: {
                                            brRq : 'IN_PSET'
                                            ,brRs : 'OUT_RSET'
                                            ,IN_PSET:[data]
                                        }
                                    });

        }
        
    }

    return (
            <>
            <pre className="tal lh12">
            <h3>Deposit Address (supporting network) (USER_DATA)</h3>
            GET /sapi/v1/capital/deposit/address (HMAC SHA256)
            Fetch deposit address with network.
            <b>Weight(IP):</b>  10
            </pre> 
            <Stack
                mt={2}
                direction="row"
                justifyContent="flex-start"
                alignItems="flex-start"
                spacing={0.5}
                >
                    <FormTextFiled  name="COIN"  control={control}  label="COIN" /> 
                    <FormTextFiled  name="NETWORK"  control={control}  label="NETWORK"  /> 
                    <Button variant="contained" color="success"  onClick={handleSubmit(fnSearch)}>조회</Button>
            </Stack>
            <TuiGrid columns={columns} ref={childRef}   />
            </>
    )
  }
