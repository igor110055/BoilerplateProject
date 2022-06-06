import TuiGrid,{TuiGridHandler} from '@/form-components/TuiGrid';
import { Button, Stack } from '@mui/material';
import { useState,useEffect, useRef, useContext } from 'react';
import { useForm } from 'react-hook-form';
import { FormTextFiled } from '@/form-components/FormTextFiled';
import { MenuContext } from '@/store/MenuStore';
import send from '@/utils/send';

interface IFormInput {
    ORDER_LIST_ID:string;
    ORIG_CLIENT_ORDER_ID:string;
}

const defaultValues = {
    ORDER_LIST_ID:"",
    ORIG_CLIENT_ORDER_ID:"",
};

export const BINANCE_0365__OCO미체결내역USER_DATA= () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm } = menuStoreContext;
    
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError,setValue } = methods;
    const childRef = useRef<TuiGridHandler>(null);
    const childRef_grid_orders = useRef<TuiGridHandler>(null);

    const fnSearchHandler= async (data: IFormInput) => {
        
        send("BR_SPOT_ACCOUNT_TRADE_GET_API_V3_QueryAllOco", {
            brRq : 'IN_PSET'
            ,brRs : 'OUT_RSET,OUT_RSET_ORDERS'
            ,IN_PSET : [ data ]
        }).then(function(data:any){
            if(childRef.current){
                childRef.current.setData(data.OUT_RSET);
            }

            if(childRef_grid_orders.current){
                childRef_grid_orders.current.setData(data.OUT_RSET_ORDERS);
            }
        }).catch(function(e){
            console.log({e})
            messageAlert(e.err_msg)

        })
    }

    return (
            <>
            <pre className="tal lh12">
            <h3>Query Open OCO (USER_DATA)</h3>
            GET /api/v3/openOrderList (HMAC SHA256)
            <b>Weight(IP):</b> 3
            </pre> 

            <Stack
                mt={2}
                direction="row"
                justifyContent="flex-start"
                alignItems="flex-start"
                spacing={0.5}
                >
                    <FormTextFiled name="FROM_ID" control={control} label="FROM_ID" sx={{width:200}}  rules={{ required: false }}   />
                    <FormTextFiled name="START_TIME" control={control} label="START_TIME" sx={{width:200}}  rules={{ required: false }}   />
                    <FormTextFiled name="END_TIME" control={control} label="END_TIME" sx={{width:200}}  rules={{ required: false }}   />
                    <FormTextFiled name="LIMIT" control={control} label="LIMIT" sx={{width:200}}  rules={{ required: false }}   />
                    
                    <Button variant="contained" color="success"   onClick={handleSubmit(fnSearchHandler)}>미체결 OCO 조회</Button>
            </Stack>
            <TuiGrid   columns={
                        [
                            {header: 'ORDER_LIST_ID',name: 'ORDER_LIST_ID',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'CONTINGENCY_TYPE',name: 'CONTINGENCY_TYPE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'LIST_STATUS_TYPE',name: 'LIST_STATUS_TYPE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'LIST_ORDER_STATUS',name: 'LIST_ORDER_STATUS',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'LIST_CLIENT_ORDER_ID',name: 'LIST_CLIENT_ORDER_ID',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'TRANSACTION_TIME',name: 'TRANSACTION_TIME',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'SYMBOL',name: 'SYMBOL',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                        ]
                        }   ref={childRef} />

            <TuiGrid   columns={
                        [
                            {header: 'SYMBOL',name: 'SYMBOL',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'ORDER_ID',name: 'ORDER_ID',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'ORDER_LIST_ID',name: 'ORDER_LIST_ID',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'CLIENT_ORDER_ID',name: 'CLIENT_ORDER_ID',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}	
                        ]
                        }   ref={childRef_grid_orders} />
            </>
    )
}
