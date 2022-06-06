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

import * as pjtUtil from '@/utils/pjtUtil'


interface IFormInput {
    CURRENCY:string;
    AL_UUIDS:string;
    AL_TXIDS:string;
    STATE:string;
}

const defaultValues = {
    CURRENCY:"",
    AL_UUIDS:"",
    AL_TXIDS:"",
    STATE:"",
};

export const UPBIT_0235__업비트출금리스트= () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm } = menuStoreContext;
    
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError,setValue } = methods;
    const childRef = useRef<TuiGridHandler>(null);

    const [upbitWithdrawsState,setUpbitWithdrawsState] = useState([]);
        
    useEffect(()=>{
        const getData = async ()=>{
            const data:any= await send("BR_CM_CD_FIND", {brRq: 'IN_DATA'
            ,brRs: 'OUT_DATA'
            ,IN_DATA: [  { GRP_CD : 'UPBIT_WITHDRAWS_STATE',  USE_YN: 'Y'}]
            });

            const tmp= data.OUT_DATA.map((m:any)=>{
                return {
                    value: m["CD"],
                    text: m["CD_NM"]            
                };
            })
            setUpbitWithdrawsState(tmp)
        }
        getData();
        handleSubmit(fnSearch)()
    },[]);
    
    const fnSearch= async (data: IFormInput) => {
        console.log({data})

        var tmp_in_pset = {
            CURRENCY : data.CURRENCY,
            STATE    : data.STATE
          }

          var tmp_arr_in_uuid = [];
          var arr_uuid_str = data.AL_UUIDS;
          if(!pjtUtil.isEmpty(arr_uuid_str)){
              var arr_tmp = arr_uuid_str.split(",");
              for(var i=0;i<arr_tmp.length;i++){
               tmp_arr_in_uuid.push({UUID : arr_tmp[i]});
              }
          }

          var tmp_arr_in_txid = [];
          var arr_txid_str = data.AL_TXIDS;
          if(!pjtUtil.isEmpty(arr_txid_str)){
              var arr_tmp = arr_txid_str.split(",");
              for(var i=0;i<arr_tmp.length;i++){
               tmp_arr_in_txid.push({TXID : arr_tmp[i]});
              }
          }
        if(childRef.current){
            childRef.current.loadData({
                                        BR:"BR_UPBIT_EXCHANGE_GET_WITHDRAWS",
                                        PARAM: {
                                            brRq : 'IN_PSET,IN_TXID,IN_UUID'
                                            ,brRs : 'OUT_RSET'
                                            ,IN_PSET:[ tmp_in_pset ]
                                            ,IN_UUID:tmp_arr_in_uuid
                                            ,IN_TXID:tmp_arr_in_txid
                                        }
                                    });
        }
    }

      const columns:OptColumn[] = [ 
        {header: '입출금 종류',name: 'TYPE',width: 140,align : 'center',filter : {type : 'text',showApplyBtn : true,showClearBtn : true}}
        ,{header: '출금의 고유 아이디',name: 'UUID',width: 140,align : 'center',filter : {type : 'text',showApplyBtn : true,showClearBtn : true}}
        ,{header: '화폐를 의미하는 영문 대문자 코드',name: 'CURRENCY',width: 100,align : 'center',filter : {type : 'text',showApplyBtn : true,showClearBtn : true},resizable: true,sortable : true,sortingType: 'desc'}
        ,{header: '출금의 트랜잭션 아이디',name: 'TXID',width: 140,align : 'right',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: '출금 상태',name: 'STATE',width: 100,align : 'center',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: '출금 생성 시간',name: 'CREATED_AT',width: 200,sortable: true,align: "center"}
        ,{header: '출금 완료 시간',name: 'DONE_AT',width: 200,sortable: true,align: "center"}
        ,{header: '출금 금액/수량',name: 'AMOUNT',width: 140,align : 'right',sortable: true}
        ,{header: '출금 수수료',name: 'FEE',width: 140,align : 'right',sortable: true}
        ,{header: '출금 유형',name: 'TRANSACTION_TYPE',width: 140,sortable: true,align: "center"}
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

                    <FormTextFiled name="CURRENCY" control={control} label="CURRENCY" sx={{width:200}}  rules={{ required: false }}   />
                    <FormSelect name="STATE" control={control} label="출금 상태" sx={{width:200}} rules={{ required: false }} options={upbitWithdrawsState}  />
                    <FormTextFiled name="AL_UUIDS" control={control} label="출금 UUID의 목록" sx={{width:200}}  rules={{ required: false }}   />
                    <FormTextFiled name="AL_TXIDS" control={control} label="출금 TXID의 목록" sx={{width:200}}  rules={{ required: false }}   />
                    <Button variant="contained" color="success"   onClick={handleSubmit(fnSearch)}>조회</Button>
            </Stack>
            <TuiGrid   columns={columns}   ref={childRef} />
            </>
    )
  }
