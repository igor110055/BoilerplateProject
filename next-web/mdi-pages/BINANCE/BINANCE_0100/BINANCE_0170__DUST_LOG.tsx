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
    START_TIME:string;
    END_TIME:string;
}

const defaultValues = {
    START_TIME:"",
    END_TIME:""
};

export const BINANCE_0170__DUST_LOG= () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm,inlineDialog,inlineDialogClose } = menuStoreContext;
    const childRef = useRef<GridHandler>(null);
    const childuserAssetDribbletsRef = useRef<GridHandler>(null);
    const childuserAssetDribbletDetailRef = useRef<GridHandler>(null);

    
    
    useEffect(()=>{

    },[])

    const [rows,setRows] = useState([]);
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError } = methods;

  
    const columns:OptColumn[] = [ 
        {header: 'TOTAL',name: 'TOTAL',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
    ];



    const columns_user_asset_dribblets:OptColumn[] = [ 
        {header: 'SEQ',name: 'SEQ',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'OPERATE_TIME',name: 'OPERATE_TIME',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'TOTAL_TRANSFERED_AMOUNT',name: 'TOTAL_TRANSFERED_AMOUNT',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'TOTAL_SERVICE_CHARGE_AMOUNT',name: 'TOTAL_SERVICE_CHARGE_AMOUNT',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'TRANS_ID',name: 'TRANS_ID',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'	}
    ];

    const columns_user_asset_dribblet_details:OptColumn[] = [ 
        {header: 'SEQ',name: 'SEQ',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'TRANS_ID',name: 'TRANS_ID',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'SERVICE_CHARGE_AMOUNT',name: 'SERVICE_CHARGE_AMOUNT',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'OPERATE_TIME',name: 'OPERATE_TIME',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'TRANSFERED_AMOUNT',name: 'TRANSFERED_AMOUNT',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'FROM_ASSET',name: 'FROM_ASSET',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
    ];

    

    const fnSearch= async (data: IFormInput) => {
        
        send("BR_BINANCE_WALLET_GET_SAPI_V1_DustLog", {
            brRq : 'IN_PSET'
            ,brRs : 'OUT_RSET,OUT_RSET_USER_ASSET_DRIBBLETS,OUT_RSET_USER_ASSET_DRIBBLET_DETAILS'
            ,IN_PSET : [ data ]
        }).then(function(data:any){
            if(childRef.current){
                childRef.current.setData(data.OUT_RSET);
                if(childuserAssetDribbletsRef.current){
                    childuserAssetDribbletsRef.current.setData(data.OUT_RSET_USER_ASSET_DRIBBLETS);
                }

                if(childuserAssetDribbletDetailRef.current){
                    childuserAssetDribbletDetailRef.current.setData(data.OUT_RSET_USER_ASSET_DRIBBLET_DETAILS);
                }
            }
        }).catch(function(e){
            console.log({e})
            messageAlert(e.err_msg)

        })
    }

    return (
            <>
            <pre className="tal lh12">
            <h3>Account Status (USER_DATA)</h3>
            GET /sapi/v1/account/status
            Fetch account status detail.
            <b>Weight(IP):</b>  1
            </pre> 
            <Stack
                mt={2}
                direction="row"
                justifyContent="flex-start"
                alignItems="flex-start"
                spacing={0.5}
                >
                    <FormTextFiled name="START_TIME" control={control} label="START_TIME" sx={{width:200}}    />
                    <FormTextFiled name="END_TIME" control={control} label="END_TIME" sx={{width:200}}   />
                    <Button variant="contained" color="success"  onClick={handleSubmit(fnSearch)}>조회</Button>
            </Stack>
            <TuiGrid columns={columns} ref={childRef}   />
            <TuiGrid columns={columns_user_asset_dribblets} ref={childuserAssetDribbletsRef}   />
            <TuiGrid columns={columns_user_asset_dribblet_details} ref={childuserAssetDribbletDetailRef}   />
            </>
    )
  }
