import TuiGrid,{TuiGridHandler} from '@/form-components/TuiGrid';
import { Button, Stack } from '@mui/material';
import { useState,useEffect, useRef, useContext } from 'react';
import { OptColumn, OptHeader } from 'tui-grid/types/options';
import {commaRenderer} from '@/form-components/tui-grid-renderer/commaRenderer'
import {commaStRenderer} from '@/form-components/tui-grid-renderer/commaStRenderer'
import {buttonRenderer} from '@/form-components/tui-grid-renderer/buttonRenderer'
import {datetimeRenderer} from '@/form-components/tui-grid-renderer/datetimeRenderer'
import {commaRendererRemoveDot} from '@/form-components/tui-grid-renderer/commaRendererRemoveDot'


import { useForm } from 'react-hook-form';
import { FormTextFiled } from '@/form-components/FormTextFiled';
import { FormSelect } from '@/form-components/FormSelect';
import { MenuContext } from '@/store/MenuStore';
import send from '@/utils/send';


interface IFormInput {
}

const defaultValues = {
};

export const UPBIT_0210__업비트전체계좌조회= () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm } = menuStoreContext;
    
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError,setValue } = methods;
    const childRef = useRef<TuiGridHandler>(null);
        
    useEffect(()=>{
        handleSubmit(fnSearch)()
    },[]);
    
    const fnSearch= async (data: IFormInput) => {
        if(childRef.current){
            childRef.current.loadData({
                                        BR:"BR_UPBIT_EXCHANGE_GET_ACCOUNTS",
                                        PARAM: {
                                            brRq : 'IN_DATA'
                                            ,brRs : 'OUT_DATA'
                                            ,IN_DATA:[data]
                                        }
                                    });
        }
    }

      const columns:OptColumn[] = [ 
        {header : '매도/매수',name : 'BTN_SELL',renderer : {type: buttonRenderer,options : {txt : '매도/매수',fn : function(el:any,data:any){}}},width : 100,sortable : true,align : "center"}
        , {header : '출금',name : 'BTN_WITHDRAW',renderer : {type: buttonRenderer,options : {txt : '출금',fn : function(el:any,data:any){}}},width : 100,sortable : true,align : "center"}
        ,{header: '화폐',name: 'CURRENCY',width: 100,align : 'center',filter : {type : 'text',showApplyBtn : true,showClearBtn : true},resizable: true,sortable : true,sortingType: 'desc'}
        ,{header: '주문가능 금액/수량',name: 'BALANCE',width: 200,align : 'right',renderer : {type: commaRenderer},sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: '주문 중 묶여있는 금액/수량',name: 'LOCKED',width: 200,align : 'right',renderer : {type: commaRenderer},sortable : true,resizable: true,sortingType: 'desc'}
        ,{ header: '매수평균가',name: 'AVG_BUY_PRICE',width: 100,align : 'right',renderer : {type: commaRenderer},sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: '매수평균가 수정 여부',name: 'AVG_BUY_PRICE_MODIFIED',width: 200,align : 'right',renderer : {type: commaRenderer},sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: '평단가 기준 화폐',name: 'UNIT_CURRENCY',width: 200,align : 'center',sortable : true,resizable: true,sortingType: 'desc'}
    ];
    return (
            <>
            <Stack
                mt={2}
                direction="row"
                justifyContent="flex-start"
                alignItems="flex-start"
                spacing={0.5}
                >
                    <FormTextFiled name="SEARCH_NM" control={control} label="검색" sx={{width:200}}  rules={{ required: false }}   />
                    <Button variant="contained" color="success"   onClick={handleSubmit(fnSearch)}>조회</Button>
            </Stack>
            <TuiGrid   columns={columns}   ref={childRef} />
            </>
    )
  }
