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

export const BINANCE_0375__계정거래목록USER_DATA= () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm } = menuStoreContext;
    
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError,setValue } = methods;
    const childRef = useRef<TuiGridHandler>(null);

    const fnSearchHandler= async (data: IFormInput) => {
        
        send("BR_SPOT_ACCOUNT_TRADE_GET_API_V3_AccountTradeList", {
            brRq : 'IN_PSET'
            ,brRs : 'OUT_RSET'
            ,IN_PSET : [ data ]
        }).then(function(data:any){
            if(childRef.current){
                childRef.current.setData(data.OUT_RSET);
            }
        }).catch(function(e){
            console.log({e})
            messageAlert(e.err_msg)
        })
    }

    return (
            <>
            <pre className="tal lh12">
            <h3>Account Trade List (USER_DATA)</h3>
            GET /api/v3/myTrades (HMAC SHA256)
            Get trades for a specific account and symbol.
            <b>Weight(IP):</b> 10
            </pre> 

            <Stack
                mt={2}
                direction="row"
                justifyContent="flex-start"
                alignItems="flex-start"
                spacing={0.5}
                >
                    <FormTextFiled name="SYMBOL" control={control} label="SYMBOL" sx={{width:200}}  rules={{ required: false }}   />
                    <FormTextFiled name="FROM_ID" control={control} label="FROM_ID" sx={{width:200}}  rules={{ required: false }}   />
                    <FormTextFiled name="START_TIME" control={control} label="START_TIME" sx={{width:200}}  rules={{ required: false }}   />
                    <FormTextFiled name="END_TIME" control={control} label="END_TIME" sx={{width:200}}  rules={{ required: false }}   />
                    <FormTextFiled name="LIMIT" control={control} label="LIMIT" sx={{width:200}}  rules={{ required: false }}   />
                    
                    <Button variant="contained" color="success"   onClick={handleSubmit(fnSearchHandler)}>계좌정보</Button>
            </Stack>
            <TuiGrid   columns={
                        [
                            {header: 'SYMBOL',name: 'SYMBOL',width: 160,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'ID',name: 'ID',width: 160,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'ORDER_ID',name: 'ORDER_ID',width: 160,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'ORDER_LIST_ID',name: 'ORDER_LIST_ID',width: 160,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'PRICE',name: 'PRICE',width: 160,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'QTY',name: 'QTY',width: 160,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'QUOTE_QTY',name: 'QUOTE_QTY',width: 160,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'COMMISSION',name: 'COMMISSION',width: 160,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'COMMISSION_ASSET',name: 'COMMISSION_ASSET',width: 160,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'TIME',name: 'TIME',width: 160,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'IS_BUYER',name: 'IS_BUYER',width: 160,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'IS_MAKER',name: 'IS_MAKER',width: 160,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'IS_BEST_MATCH',name: 'IS_BEST_MATCH',width: 160,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                        ]
                        }   ref={childRef} />
            </>
    )
}
