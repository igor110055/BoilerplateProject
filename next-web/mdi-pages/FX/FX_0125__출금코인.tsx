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
    EXCEPT_YN:string;
    BINANCE_DEPOSIT_REG_YN:string;
    UPBIT_DEPOSIT_REG_YN:string;
}

const defaultValues = {
    EXCEPT_YN:"",
    BINANCE_DEPOSIT_REG_YN:"",
    UPBIT_DEPOSIT_REG_YN:"",
};


                /*
                searchForm.setData("WITHDRAW_ENABLE","Y");
                searchForm.checked("WITHDRAW_ENABLE");
                */


export const FX_0125__출금코인= () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm } = menuStoreContext;
    
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError } = methods;

    const [useYn,setUseYn] = useState([]);
    const childRef = useRef<TuiGridHandler>(null);


    useEffect(()=>{
        const getData = async ()=>{
            const data:any= await send("BR_CM_CD_FIND", {brRq: 'IN_DATA'
            ,brRs: 'OUT_DATA'
            ,IN_DATA: [  { GRP_CD : 'USE_YN',  USE_YN: 'Y'}]
            });

            const tmp= data.OUT_DATA.map((m:any)=>{
                return {
                    value: m["CD"],
                    text: m["CD_NM"]            
                };
            })
            setUseYn(tmp);
        }
        getData();
        handleSubmit(fnSearch)()
    },[]);

    
    const fnSearch= async (data: IFormInput) => {
        if(childRef.current){
            childRef.current.loadData({
                                        BR:"BR_FX_COIN_WITHDRAW_retrieveCurrency",
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
             {header: '업비트주소',name: 'mergeColumn1',childNames: ['UPBIT_DEPOSIT_ADDRESS','UPBIT_DEPOSIT_ADDRESS_SECOND','BINANCE_DEPOSIT_REG_YN']} 
            ,{header: '바이낸스',name: 'mergeColumn2',childNames: ['BINANCE_DEPOSIT_ADDRESS','BINANCE_DEPOSIT_ADDRESS_SECOND','UPBIT_DEPOSIT_REG_YN']}             
            ]    
    }

      const columns:OptColumn[] = [ 
         {header : '코인(통화)',name : 'CURRENCY',width : 100,resizable : false,sortable : true,validation : {dataType : 'string', /*string ,number*/required : true, /*  true 필수, false 필수아님  */unique : true}}
        ,{header : '코인명',name : 'CURRENCY_NM',width : 100,resizable : false,sortable : true}
        ,{header : '코인명(EN)',name : 'CURRENCY_NM_EN',width : 100,resizable : false,sortable : true}
        ,{header : 'U_YN',name : 'UPBIT_YN',width : 40,resizable : false,align : "center"}
        ,{header : 'B_YN',name : 'BINANCE_YN',width : 40,resizable : false,align : "center"}
        ,{header : 'TIME',name : 'TIME_GAP',width : 60,resizable : false,sortable : true,editor : 'text',align : "center"}
        ,{header : '비고',name : 'RMK',width : 60,resizable : true,editor : 'text'}
        ,{header : '제외여부',name : 'EXCEPT_YN',width : 70,resizable : false,sortable : true,editor: {type: 'radio',options: {listItems: [{ text: 'Y', value: 'Y' },{ text: 'N', value: 'N' }]}},align : "center" }
        ,{header : '제외사유',name : 'EXCEPT_RSN',width : 180,resizable : false,editor : 'text'},{header : '입금주소',name : 'UPBIT_DEPOSIT_ADDRESS',width : 240,resizable : true}
        ,{header : '입금2차주소',name : 'UPBIT_DEPOSIT_ADDRESS_SECOND',width : 100,resizable : true},{header : '바이낸스-등록',name : 'BINANCE_DEPOSIT_REG_YN',width : 80,editor: {type: 'radio',options: {listItems: [{ text: 'Y', value: 'Y' },{ text: 'N', value: 'N' }]}},align : "center"}
        ,{header : '입금주소',name : 'BINANCE_DEPOSIT_ADDRESS',width : 240,resizable : true}
        ,{header : '입금2차주소',name : 'BINANCE_DEPOSIT_ADDRESS_SECOND',width : 100,resizable : true}
        ,{header : '업비트-등록',name : 'UPBIT_DEPOSIT_REG_YN',width : 80,editor: {type: 'radio',options: {listItems: [{ text: 'Y', value: 'Y' },{ text: 'N', value: 'N' }]}},align : "center"}
        ,{header : '생성일',name : 'CRT_DTM',renderer : {type : datetimeRenderer,options : {format : 'yyyy-MM-DD HH:mm' /*YYYYMMDDHHmmss    이게 풀양식이다.*/,source : 'YYYYMMDDHHmmss' /*TIME 초, YYYYMMDD , YYYYMMDDHHmm,  YYYYMMDDHHmmss  */}},width : 100,align : "center"}
        ,{header : '수정일',name : 'UPDT_DTM',renderer : {type : datetimeRenderer,options : {format : 'yyyy-MM-DD HH:mm' /*YYYYMMDDHHmmss    이게 풀양식이다.*/,source : 'YYYYMMDDHHmmss' /*TIME 초, YYYYMMDD , YYYYMMDDHHmm,  YYYYMMDDHHmmss  */}},width : 100,align : "center"/*,  filter: 'number'  숫자일경우 비교 */} 
    ];

    
    
    const saveHandler=(event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
        if(childRef.current){
            const data = childRef.current.getModifiedRows();
            var crt_cnt	= data.createdRows.length;
            var updt_cnt= data.updatedRows.length;
            if((crt_cnt+updt_cnt)==0) {
                messageAlert("저장할 내용이 존재하지 않습니다.");
                return;
            }
            messageConfirm("저장하시겠습니까?",function()  {
                //_this.showProgress();	
                send('BR_FX_COIN_WITHDRAW_saveCurrencyExcept',{
                    brRq 		: 'UPDT_DATA'
                    ,brRs 		: ''
                    ,UPDT_DATA	: data.updatedRows
                }).then(function(data){
                    //_this.hideProgress();
                    if(data){
                        messageAlert("저장되었습니다",function()  {
                            handleSubmit(fnSearch)()
                        });
                    }
                })                      
            })
        }
    }

    const delHandler=(event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
        if(childRef.current){
            const data = childRef.current.getCheckedData();
            console.log({data});
            if(data.length<=0) {
                messageAlert('선택된 항목이 없습니다.');
                return;
            }
            messageConfirm("삭제하시겠습니까?",function()  {
                //_this.showProgress();	
                send('BR_FX_COIN_WITHDRAW_removeCurrency',{
                    brRq 		: 'IN_PSET'
                    ,brRs 		: ''
                    ,IN_DATA	: data
                }).then(function(data){
                    //_this.hideProgress();
                    if(data){
                        messageAlert("삭제되었습니다",function()  {
                            handleSubmit(fnSearch)()
                        });
                    }
                })                      
            })
        }
    }

    const syncHandler=(event: React.MouseEvent<HTMLButtonElement>) => {

        messageConfirm("싱크하시겠습니까?",function()  {
            //_this.showProgress();	
            send('BR_FX_COIN_WITHDRAW_syncCurrency',{
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
                    <FormTextFiled name="CURRENCY" control={control} label="CURRENCY" sx={{width:200}}  rules={{ required: false }}   />
                    <FormSelect name="EXCEPT_YN" control={control} label="제외여부" sx={{width:200}} rules={{ required: false }}  options={useYn}  />
                    <FormSelect name="BINANCE_DEPOSIT_REG_YN" control={control} label="업비트주소-바이낸스등록" sx={{width:200}} rules={{ required: false }}  options={useYn}  />
                    <FormSelect name="UPBIT_DEPOSIT_REG_YN" control={control} label="바이낸스주소-업비트등록" sx={{width:200}} rules={{ required: false }} options={useYn}  />
                    
                    <Button variant="contained" color="success"   onClick={handleSubmit(fnSearch)}>조회</Button>
                    <Button variant="contained" color="success" onClick={syncHandler}>싱크</Button>
                    <Button variant="contained" color="success"  onClick={saveHandler}>저장</Button>                                                                        
                    <Button variant="contained" color="success"  onClick={delHandler}>삭제</Button>                                                                        
            </Stack>
            <TuiGrid   columns={columns}   ref={childRef} header={header} />
            </>
    )
  }
function messageAlert(arg0: string) {
    throw new Error('Function not implemented.');
}

