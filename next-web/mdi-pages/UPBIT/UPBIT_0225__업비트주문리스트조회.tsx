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
    MARKET:string;
    AL_STATES:string;
    UUIDS:string;
    IDENTIFIERS:string;
}

const defaultValues = {
    MARKET:"",
    AL_STATES:"",
    UUIDS:"",
    IDENTIFIERS:"",
};

export const UPBIT_0225__업비트주문리스트조회= () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm } = menuStoreContext;
    
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError,setValue } = methods;
    const childRef = useRef<TuiGridHandler>(null);

    const [alStates,setAlStates] = useState([]);
        
    useEffect(()=>{
        const getData = async ()=>{
            const data:any= await send("BR_CM_CD_FIND", {brRq: 'IN_DATA'
            ,brRs: 'OUT_DATA'
            ,IN_DATA: [  { GRP_CD : 'UPBIT_ORDER_STATES',  USE_YN: 'Y'}]
            });

            const tmp= data.OUT_DATA.map((m:any)=>{
                return {
                    value: m["CD"],
                    text: m["CD_NM"]            
                };
            })
            setAlStates(tmp)
        }
        getData();
        handleSubmit(fnSearch)()
    },[]);
    
    const fnSearch= async (data: IFormInput) => {
        console.log({data})

        var tmp_in_pset = {
            MARKET : data.MARKET
       }
       var tmp_arr_in_state = [];
       var arr_state_str = data.AL_STATES;
       if(!pjtUtil.isEmpty(arr_state_str)){
           var arr_tmp = arr_state_str.split(",");
           for(var i=0;i<arr_tmp.length;i++){
            tmp_arr_in_state.push({STATE : arr_tmp[i]});
           }
       }
       var tmp_arr_in_uuid = [];
       var arr_uuid_str = data.UUIDS;
       if(!pjtUtil.isEmpty(arr_uuid_str)){           
           var arr_tmp = arr_uuid_str.split(",");
           for(var i=0;i<arr_tmp.length;i++){
            tmp_arr_in_uuid.push({UUID : arr_tmp[i]});
           }
       }

       var tmp_arr_in_identifiers = [];
       var arr_uuid_identifiers = data.IDENTIFIERS;
       if(!pjtUtil.isEmpty(arr_uuid_identifiers)){                
           var arr_tmp = arr_uuid_identifiers.split(",");
           for(var i=0;i<arr_tmp.length;i++){
            tmp_arr_in_identifiers.push({IDENTIFIERS : arr_tmp[i]});
           }
       }
        if(childRef.current){
            childRef.current.loadData({
                                        BR:"BR_UPBIT_EXCHANGE_GET_ORDERS",
                                        PARAM: {
                                            brRq : 'IN_PSET,IN_STATE,IN_UUID,IN_IDENTIFIER'
                                            ,brRs : 'OUT_RSET'
                                            ,IN_PSET:[ tmp_in_pset ]
                                            ,IN_STATE:tmp_arr_in_state
                                            ,IN_UUID:tmp_arr_in_uuid
                                            ,IN_IDENTIFIER:tmp_arr_in_identifiers
                                        }
                                    });
        }
    }

      const columns:OptColumn[] = [ 
        {header: '주문의 고유 아이디',name: 'UUID',width: 140,align : 'center',filter : {type : 'text',showApplyBtn : true,showClearBtn : true}}
        ,{header: '주문 종류',name: 'SIDE',width: 100,align : 'center',filter : {type : 'text',showApplyBtn : true,showClearBtn : true}}
        ,{header: '주문 방식',name: 'ORD_TYPE',width: 100,align : 'center',filter : {type : 'text',showApplyBtn : true,showClearBtn : true},resizable: true,sortable : true,sortingType: 'desc'}
        ,{header: '주문 당시 화폐 가격',name: 'PRICE',width: 140,align : 'right',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: '주문 상태',name: 'STATE',width: 100,align : 'center',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: '마켓의 유일키',name: 'MARKET',width: 200,sortable: true,align: "center"}
        ,{header: '주문 생성 시간',name: 'CREATED_AT',width: 200,sortable: true,align: "center"}
        ,{header: '사용자가 입력한 주문 양',name: 'VOLUME',width: 140,sortable: true,align: "right"}
        ,{header: '체결 후 남은 주문 양',name: 'REMAINING_VOLUME',width: 140,align : 'right',sortable: true}
        ,{header: '수수료로 예약된 비용',name: 'RESERVED_FEE',width: 140,align : 'right',sortable: true}
        ,{header: '남은 수수료',name: 'REMAINING_FEE',width: 140,sortable: true,align: "center"}
        ,{header: '사용된 수수료',name: 'PAID_FEE',width: 140,align : 'right',sortable: true}
        ,{header: '거래에 사용중인 비용',name: 'LOCKED',width: 140,sortable: true,align: "center"}
        ,{header: '체결된 양',name: 'EXECUTED_VOLUME',width: 200,align : 'right',sortable: true}
        ,{header: '해당 주문에 걸린 체결 수',name: 'TRADE_COUNT',width: 140,align : 'right',sortable: true}
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

                    <FormTextFiled name="MARKET" control={control} label="마켓" sx={{width:200}}  rules={{ required: false }}   />
                    <FormSelect name="AL_STATES" control={control} label="주문상태" sx={{width:200}} rules={{ required: false }} options={alStates}  />
                    <FormTextFiled name="UUIDS" control={control} label="주문UUID목록" sx={{width:200}}  rules={{ required: false }}   />
                    <FormTextFiled name="IDENTIFIERS" control={control} label="주문IDENTIFIERS목록" sx={{width:200}}  rules={{ required: false }}   />
                    <Button variant="contained" color="success"   onClick={handleSubmit(fnSearch)}>조회</Button>
            </Stack>
            <TuiGrid   columns={columns}   ref={childRef} />
            </>
    )
  }
