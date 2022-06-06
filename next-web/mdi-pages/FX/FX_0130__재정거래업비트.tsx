import TuiGrid,{TuiGridHandler} from '@/form-components/TuiGrid';
import { Button, Stack } from '@mui/material';
import { useState,useEffect, useRef, useContext } from 'react';
import { OptColumn, OptHeader } from 'tui-grid/types/options';
import {commaRenderer} from '@/form-components/tui-grid-renderer/commaRenderer'
import {commaStRenderer} from '@/form-components/tui-grid-renderer/commaStRenderer'
import {buttonRenderer} from '@/form-components/tui-grid-renderer/buttonRenderer'
import {datetimeRenderer} from '@/form-components/tui-grid-renderer/datetimeRenderer'
import { useForm } from 'react-hook-form';
import { FormTextFiled } from '@/form-components/FormTextFiled';
import { FormSelect } from '@/form-components/FormSelect';
import { MenuContext } from '@/store/MenuStore';
import send from '@/utils/send';


interface IFormInput {
    START_AMT:string;
    END_AMT:string;
}

const defaultValues = {
    START_AMT:"",
    END_AMT:"",
};




export const FX_0130__재정거래업비트= () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm } = menuStoreContext;
    
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError,setValue } = methods;
    const childRef = useRef<TuiGridHandler>(null);
    const [balanceType,setBalanceType] = useState([]);

    setValue("START_AMT","900000");
    setValue("END_AMT","1000000");

    useEffect(()=>{
        const getData = async ()=>{
            const data:any= await send("BR_CM_CD_FIND", {brRq: 'IN_DATA'
            ,brRs: 'OUT_DATA'
            ,IN_DATA: [  { GRP_CD : 'BALANCE_TYPE',  USE_YN: 'Y'}]
            });

            const tmp= data.OUT_DATA.map((m:any)=>{
                return {
                    value: m["CD"],
                    text: m["CD_NM"]            
                };
            })
            setBalanceType(tmp)
        }
        getData();
        handleSubmit(fnSearch)()
    },[]);

    
    const fnSearch= async (data: IFormInput) => {
        if(childRef.current){
            childRef.current.loadData({
                                        BR:"BR_FX_retrieveTmpUpbit",
                                        PARAM: {
                                            brRq : 'IN_PSET'
                                            ,brRs : 'OUT_RSET'
                                            ,IN_PSET:[data]
                                        }
                                    });

        }
        
    }

    const header:OptHeader =   {
        height: 60,
        complexColumns: [
            {header: '업비트-단가',name: 'mergeColumn1',childNames: ['COIN','MARKET','SYMBOL','UPBIT_PRICE','BINANCE_PRICE_KRW','BINANCE_PRICE_USD','GAP','PER']}
            ,{header: '업비트-합',name: 'mergeColumn2',childNames: ['CNT','UPBIT_WITHDRAW_FEE_AMT','UPBIT_BID_FEE_AMT','UPBIT_SUM_PRICE','UPBIT_SUM_AMT','BINANCE_SUM_AMT','SUM_GAP','SUM_PER']}       
            ,{header: '잔액',name: 'mergeColumn0',childNames: ['BALANCE_TYPE','UPBIT_KRW_BALANCE','MIN_KRW_BALANCE','BASE_KRW_BALANCE']}                  
            ]  
    }

      const columns:OptColumn[] = [ 
        {header: 'FX_SEQ',name: 'FX_SEQ',width: 100,align : 'center',sortable : false,resizable: true,sortingType: 'desc'}
        ,{header: 'R',name: 'RNK',width: 30,align : 'right',sortable : false,resizable: true,sortingType: 'desc'}
        ,{header: '(C)',name: 'COIN_RNK',width: 30,align : 'right',sortable : false,resizable: true,sortingType: 'desc'}
        ,{header: '코인',name: 'COIN',width: 80,align : 'center',sortable : false,resizable: true,sortingType: 'desc'}
        ,{header: '업비트',name: 'MARKET',width: 100,align : 'center',sortable : false,resizable: true,sortingType: 'desc'}
        ,{header: '바이낸스',name: 'SYMBOL',width: 100,align : 'center',sortable : false,resizable: true,sortingType: 'desc'}
        ,{header: '업비트',name: 'UPBIT_PRICE',width: 60,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaRenderer}}
        ,{header: '바이낸스',name: 'BINANCE_PRICE_KRW',width: 80,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaRenderer}}
        ,{header: '$바이낸스',name: 'BINANCE_PRICE_USD',width: 80,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaRenderer}}
        ,{header: 'GAP',name: 'GAP',width: 60,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaStRenderer,options : {src : 'BINANCE_PRICE_KRW',tgt : 'UPBIT_PRICE'}}}
        ,{header: 'PER',name: 'PER',width: 60,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaStRenderer,options : {src : 'BINANCE_PRICE_KRW',tgt : 'UPBIT_PRICE'}}}
        ,{header: '수량',name: 'CNT',width: 100,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaRenderer}}
        ,{header: '수수료',name: 'UPBIT_BID_FEE_AMT',width: 80,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaRenderer}}
        ,{header: '출금',name: 'UPBIT_WITHDRAW_FEE_AMT',width: 60,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaRenderer}}
        ,{header: '업비트(수X)',name: 'UPBIT_SUM_PRICE',width: 80,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaRenderer}}
        ,{header: '업비트(수O)',name: 'UPBIT_SUM_AMT',width: 80,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaRenderer}}
        ,{header: '바이낸스',name: 'BINANCE_SUM_AMT',width: 80,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaRenderer}}
        ,{header: 'GAP',name: 'SUM_GAP',width: 60,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaStRenderer,options : {src : 'BINANCE_SUM_AMT',tgt : 'UPBIT_SUM_AMT'}}}
        ,{header: 'PER',name: 'SUM_PER',width: 60,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaStRenderer,options : {src : 'BINANCE_SUM_AMT',tgt : 'UPBIT_SUM_AMT'}}}
        ,{header: '최소구매C',name: 'UPBIT_MIN_CNT',width: 60,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaRenderer}}
        ,{header: '최소출금C',name: 'UPBIT_WITHDRAW_MINIMUN',width: 60,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaRenderer}}
        ,{header: '최소구매금액',name: 'UPBIT_BID_MIN_TOTAL',width: 70,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaRenderer}}
        ,{header: '(B)수수료율',name: 'BINANCE_MAKER_COMMISSION',width: 70,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaRenderer}}
        ,{header: '(U)수수료율',name: 'UPBIT_BID_FEE',width: 70,align : 'right',sortable : false,resizable: true,renderer : {type: commaRenderer}}
        ,{header: '환율',name: 'KRW_USD',width: 70,align : 'right',sortable : false,resizable: true,renderer : {type: commaRenderer}}
        ,{header: 'TYPE',name: 'BALANCE_TYPE',width: 60,align : 'center',sortable : false,resizable: true,sortingType: 'desc'}
        ,{header: 'U_KRW',name: 'UPBIT_KRW_BALANCE',width: 80,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaRenderer}}
        ,{header: 'MIN_KRW',name: 'MIN_KRW_BALANCE',width: 80,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaRenderer}}
        ,{header: 'BASE_KRW',name: 'BASE_KRW_BALANCE',width: 80,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaRenderer}}
        ,{header : '생성일',name : 'CRT_DTM',width : 100,sortable : false,align : "center",renderer : {type : datetimeRenderer,options : {format : 'yyyy-MM-DD HH:mm' /*YYYYMMDDHHmmss    이게 풀양식이다.*/,source : 'YYYYMMDDHHmmss' /*TIME 초, YYYYMMDD , YYYYMMDDHHmm,  YYYYMMDDHHmmss  */}}}
    ];

    const syncHandler=(event: React.MouseEvent<HTMLButtonElement>) => {

        messageConfirm("싱크하시겠습니까?",function()  {
            //_this.showProgress();	
            send('BR_FX_TMP_calculate',{
                brRq 		: ''
                ,brRs 		: ''
            }).then(function(data){
                //_this.hideProgress();
                if(data){
                    messageAlert("싱크되었습니다",function()  {
                        handleSubmit(fnSearch)()
                    });
                }
            })                      
        })
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
                    <FormSelect name="BALANCE_TYPE" control={control} label="BALANCE_TYPE" sx={{width:200}} rules={{ required: false }} options={balanceType}  />
                    <FormTextFiled name="COIN" control={control} label="COIN" sx={{width:200}}  rules={{ required: false }}   />
                    <FormTextFiled name="START_AMT" control={control} label="START_AMT" sx={{width:200}}  rules={{ required: false }}   />                    
                    <FormTextFiled name="END_AMT" control={control} label="END_AMT" sx={{width:200}}  rules={{ required: false }}   />
                    
                    <Button variant="contained" color="success"   onClick={handleSubmit(fnSearch)}>조회</Button>
                    <Button variant="contained" color="success" onClick={syncHandler}>싱크</Button>
            </Stack>
            <TuiGrid   columns={columns}   ref={childRef} header={header} />
            </>
    )
  }
function messageAlert(arg0: string) {
    throw new Error('Function not implemented.');
}

