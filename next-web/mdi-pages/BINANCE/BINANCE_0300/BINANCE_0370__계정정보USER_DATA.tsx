import TuiGrid,{TuiGridHandler} from '@/form-components/TuiGrid';
import { Button, Stack } from '@mui/material';
import { useState,useEffect, useRef, useContext } from 'react';
import { useForm } from 'react-hook-form';
import { FormTextFiled } from '@/form-components/FormTextFiled';
import { MenuContext } from '@/store/MenuStore';
import send from '@/utils/send';
import { buttonRenderer } from '@/form-components/tui-grid-renderer/buttonRenderer';

interface IFormInput {
    ORDER_LIST_ID:string;
    ORIG_CLIENT_ORDER_ID:string;
}

const defaultValues = {
    ORDER_LIST_ID:"",
    ORIG_CLIENT_ORDER_ID:"",
};

export const BINANCE_0370__계정정보USER_DATA= () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm } = menuStoreContext;
    
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError,setValue } = methods;
    const childRef = useRef<TuiGridHandler>(null);
    const childRef_grid_permissions = useRef<TuiGridHandler>(null);
    const childRef_grid_balances = useRef<TuiGridHandler>(null);

    const fnSearchHandler= async (data: IFormInput) => {
        
        send("BR_SPOT_ACCOUNT_TRADE_GET_API_V3_AccountInformation", {
            brRq : 'IN_PSET'
            ,brRs : 'OUT_RSET,OUT_RSET_BALANCES,OUT_RSET_PERMISSIONS'
            ,IN_PSET : [ data ]
        }).then(function(data:any){
            if(childRef.current){
                childRef.current.setData(data.OUT_RSET);
            }

            if(childRef_grid_permissions.current){
                childRef_grid_permissions.current.setData(data.OUT_RSET_PERMISSIONS);
            }

            if(childRef_grid_balances.current){
                childRef_grid_balances.current.setData(data.OUT_RSET_BALANCES);
            }
        }).catch(function(e){
            console.log({e})
            messageAlert(e.err_msg)
        })
    }

    return (
            <>
            <pre className="tal lh12">
            <h3>Account Information (USER_DATA)</h3>
            GET /api/v3/account (HMAC SHA256)
            Get current account information.
            <b>Weight(IP):</b> 10
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
                    
                    <Button variant="contained" color="success"   onClick={handleSubmit(fnSearchHandler)}>조회</Button>
            </Stack>
            <TuiGrid   columns={
                        [
                            {header: 'MAKER_COMMISSION',name: 'MAKER_COMMISSION',width: 130,align : 'right',sortable : false,resizable: true,sortingType: 'desc'}
                            ,{header: 'TAKER_COMMISSION',name: 'TAKER_COMMISSION',width: 130,align : 'right',sortable : false,resizable: true,sortingType: 'desc'}
                            ,{header: 'BUYER_COMMISSION',name: 'BUYER_COMMISSION',width: 130,align : 'right',sortable : false,resizable: true,sortingType: 'desc'}
                            ,{header: 'SELLER_COMMISSION',name: 'SELLER_COMMISSION',width: 130,align : 'right',sortable : false,resizable: true,sortingType: 'desc'}
                            ,{header: 'CAN_TRADE',name: 'CAN_TRADE',width: 80,align : 'center',sortable : false,resizable: true,sortingType: 'desc'}
                            ,{header: 'CAN_WITHDRAW',name: 'CAN_WITHDRAW',width: 100,align : 'center',sortable : false,resizable: true,sortingType: 'desc'}
                            ,{header: 'CAN_DEPOSIT',name: 'CAN_DEPOSIT',width: 100,align : 'center',sortable : false,resizable: true,sortingType: 'desc'}
                            ,{header: 'UPDATE_TIME',name: 'UPDATE_TIME',width: 100,align : 'center',sortable : false,resizable: true,sortingType: 'desc'}
                            ,{header: 'ACCOUNT_TYPE',name: 'ACCOUNT_TYPE',width: 100,align : 'center',sortable : false,resizable: true,sortingType: 'desc'}
                        ]
                        }   ref={childRef} />

            <TuiGrid   columns={
                        [
                            {header: 'TYPE',name: 'TYPE',width: 80,align : 'center',sortable : true,resizable: true,sortingType: 'desc'}	
                        ]
                        }   ref={childRef_grid_permissions} />
            <TuiGrid   columns={
                        [
                            {header : '(B)매도/매수',name : 'BTN_BINANCE_SELL',renderer : {type: buttonRenderer,options : {txt : '매도/매수',fn : function(el:any,data:any){}}},width : 90,sortable : false,align : "center"}
                            ,{header : '(B)출금',name : 'BTN_BINACE_WITHDRAW',renderer : {type: buttonRenderer,options : {txt : '출금',fn : function(el:any,data:any){}}},width : 90,sortable : false,align : "center"}
                            ,{header: 'ASSET',name: 'ASSET',width: 100,align : 'center',sortable : false,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'FREE',name: 'FREE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'LOCKED',name: 'LOCKED',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}	
                        ]
                        }   ref={childRef_grid_balances} />                        
            </>
    )
}
