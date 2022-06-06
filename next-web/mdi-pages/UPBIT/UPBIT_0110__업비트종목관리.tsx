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
    MARKET_CD:string;
    SEARCH_NM:string;
    MARKET_WARNING:string;
}

const defaultValues = {
    MARKET_CD:"",
    SEARCH_NM:"",
    MARKET_WARNING:"",
};

export const UPBIT_0110__업비트종목관리= () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm } = menuStoreContext;
    
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError,setValue } = methods;
    const childRef = useRef<TuiGridHandler>(null);

    const [marketCd,setMarketCd] = useState([]);
    const [marketWarning,setMarketWarning] = useState([]);
        
    useEffect(()=>{
        const getData = async ()=>{
            const data:any= await send("BR_CM_CD_FIND", {brRq: 'IN_DATA'
            ,brRs: 'OUT_DATA'
            ,IN_DATA: [  { GRP_CD : 'UPBIT_MARKET_CD',  USE_YN: 'Y'}]
            });

            const tmp= data.OUT_DATA.map((m:any)=>{
                return {
                    value: m["CD"],
                    text: m["CD_NM"]            
                };
            })
            setMarketCd(tmp)

            const data2:any= await send("BR_CM_CD_FIND", {brRq: 'IN_DATA'
            ,brRs: 'OUT_DATA'
            ,IN_DATA: [  { GRP_CD : 'UPBIT_MARKET_WARNING',  USE_YN: 'Y'}]
            });

            const tmp2= data2.OUT_DATA.map((m:any)=>{
                return {
                    value: m["CD"],
                    text: m["CD_NM"]            
                };
            })
            setMarketWarning(tmp2)
        }
        getData();
        handleSubmit(fnSearch)()
    },[]);

    
    const fnSearch= async (data: IFormInput) => {
        if(childRef.current){
            childRef.current.loadData({
                                        BR:"BR_UPBIT_MARKET_FIND",
                                        PARAM: {
                                            brRq : 'IN_DATA'
                                            ,brRs : 'OUT_DATA'
                                            ,IN_DATA:[data]
                                        }
                                    });

        }
        
    }

    const syncHandler=(event: React.MouseEvent<HTMLButtonElement>) => {

        messageConfirm("싱크하시겠습니까?",function()  {
            //_this.showProgress();	
            send('BR_UPBIT_MARKET_SYNC',{
                brRq 		: 'IN_DATA'
                ,brRs 		: ''
                ,IN_DATA:[ {} ]
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


      const columns:OptColumn[] = [ 
        {header: '코인',name: 'COIN',width: 140,align : 'center',filter : {type : 'text',showApplyBtn : true,showClearBtn : true}}
        ,{header: 'MARKET_CD',name: 'MARKET_CD',width: 140,align : 'center',filter : {type : 'text',showApplyBtn : true,showClearBtn : true}}
        ,{header: '종목명',name: 'MARKET',width: 200,align : 'center',filter : {type : 'text',showApplyBtn : true,showClearBtn : true}}
        ,{header: '영문명',name: 'EN_NM',width: 200,align : 'center',filter : {type : 'text',showApplyBtn : true,showClearBtn : true},resizable: true,sortable : true,sortingType: 'desc'}
        ,{header: '한글명',name: 'KR_NM',width: 200,align : 'center',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: '유의 종목 여부',name: 'MARKET_WARNING',width: 200,align : 'center',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: '생성일',name: 'CRT_DTM',width: 200,sortable: true,align: "center"}
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
                    <FormSelect name="MARKET_CD" control={control} label="업비트 통화코드" sx={{width:200}} rules={{ required: false }} options={marketCd}  />
                    <FormTextFiled name="SEARCH_NM" control={control} label="검색" sx={{width:200}}  rules={{ required: false }}   />
                    <FormSelect name="MARKET_WARNING" control={control} label="유의 종목 여부" sx={{width:200}} rules={{ required: false }} options={marketWarning}  />
                    <Button variant="contained" color="success"   onClick={handleSubmit(fnSearch)}>조회</Button>
                    <Button variant="contained" color="success" onClick={syncHandler}>싱크</Button>
                    업비트는 오전 9시에 종가 초기화
            </Stack>
            <TuiGrid   columns={columns}   ref={childRef} />
            </>
    )
  }
function messageAlert(arg0: string) {
    throw new Error('Function not implemented.');
}

