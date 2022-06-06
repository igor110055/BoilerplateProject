import Grid,{GridHandler} from '@/form-components/Grid';
import GridCombo from '@/form-components/GridCombo';
import GridTextEditor from '@/form-components/GridTextEditor';
import { useForm } from "react-hook-form";
import {FormSelect}  from '@/form-components/FormSelect';
import {FormTextFiled} from '@/form-components/FormTextFiled';
import React, { useState,useEffect,useRef,useReducer,useContext,useImperativeHandle,forwardRef, InputHTMLAttributes, DetailedHTMLProps} from "react";
import send from '@/utils/send';
import {Stack,Button} from '@mui/material'
import {MenuContext} from "@/store/MenuStore";
import { CM_2300__사용자조회 } from './CM_2300__사용자조회';
import { CalculatedColumn } from 'react-data-grid';

interface IFormInput {
}

const defaultValues = {
};

export  const CM_1900__코인계좌정보 = () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm } = menuStoreContext;

    const childRef = useRef<GridHandler>(null);
    const [useYnData,setUseYnData] = useState([]);
    const [coinCd,setCoinCd] = useState([]);
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError } = methods;
    const fnSearch= async (data: IFormInput) => {
        if(childRef.current){
            childRef.current.loadData({
                                        BR:"BR_COIN_retrieveCoinAcctMngrList",
                                        PARAM: {
                                            brRq : 'IN_DATA'
                                            ,brRs : 'OUT_DATA'
                                            ,IN_DATA:[data]
                                        }
                                    });

        }
    }
    useEffect(()=>{
        const getInitData = async ()=>{
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
            setUseYnData(tmp)

            const data2:any= await send("BR_CM_CD_FIND", {brRq: 'IN_DATA'
            ,brRs: 'OUT_DATA'
            ,IN_DATA: [  { GRP_CD : 'COIN_CD',  USE_YN: 'Y'}]
            });

            const tmp2= data2.OUT_DATA.map((m:any)=>{
                return {
                    value: m["CD"],
                    text: m["CD_NM"]            
                };
            })
            setCoinCd(tmp2)
        }
        getInitData()
        handleSubmit(fnSearch)()
    },[]);

    const columns = [
        { key: 'SEQ', name: 'SEQ', width: 80 ,  sortable: true, dataType:"number"  },
        { key: 'COIN_CD', name: '코인코드', width: 100  ,editor: (p:any) => (<GridCombo p={p} options={coinCd}  />) , editorOptions: {editOnClick: true}},
        { key: 'USER_UID', name: '사용자UID', width: 80  ,editor: (p:any) =>(<GridPopup p={p}   />) ,editorOptions: {editOnClick: true} },
        { key: 'USE_YN', name: '사용여부', width: 80  ,editor: (p:any) => (<GridCombo p={p} options={useYnData}  />) , editorOptions: {editOnClick: true}},
        { key: "ACCESS_KEY", name: 'API접속키', width: 540 ,  sortable: true  ,editor: GridTextEditor},
        { key: 'SECRET_KEY', name: '비밀키', width: 500 ,  sortable: true ,editor: GridTextEditor},
        { key: 'RMK', name: '비고', width: 80 ,  sortable: true ,editor: GridTextEditor},
        { key: 'CRT_DTM', name: '생성일', width: 160 },
        { key: 'UPDT_DTM', name: '수정일', width: 160 },
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
                send('BR_COIN_saveCoinAcctMngr',{
                    brRq 		: 'IN_DATA,UPDT_DATA,SESSION'
                    ,brRs 		: ''
                    ,IN_DATA	: data.createdRows
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
                send('BR_COIN_removeCoinAcctMngr',{
                    brRq 		: 'IN_DATA,SESSION'
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

    


    return (
            <>
            <Stack
                mt={2}
                direction="row"
                justifyContent="flex-start"
                alignItems="flex-start"
                spacing={0.5}
                >
                    <Button variant="contained" color="success"   onClick={handleSubmit(fnSearch)}>조회</Button>
                    <Button variant="contained" color="success" onClick={() => {
                                                                            if(childRef.current){
                                                                                const tmp = childRef.current.addRow();
                                                                                console.log({tmp})
                                                                            }}
                                                                        }>추가</Button>
                    <Button variant="contained" color="success"  onClick={saveHandler}>저장</Button>                                                                        
                    <Button variant="contained" color="success"  onClick={delHandler}>삭제</Button>                                                                        
            </Stack>
            <Grid style={{ height: 400, width: '100%' }} columns={columns}  checkbox={true} showRowStatus={true} ref={childRef} />
            </>
    )
  }
  
  



  const GridPopup = (props:any) =>{
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm,inlineDialog,inlineDialogClose } = menuStoreContext;
    const clickHandler =(e:DetailedHTMLProps<InputHTMLAttributes<HTMLInputElement>, HTMLInputElement> )=>{
        const gridOnRowDoubleClickHandler  = (row: any, column2: CalculatedColumn<any, any>)=>{
            const tmp =(props.p.row  as any)["_row_status"];
            let row_status="";
            if(tmp=="C"){
                row_status="C";
            } else {
                row_status="U";
            }
            //사용자번호
            //사용자명
            //사용자ID
            props.p.onRowChange({ ...props.p.row
                ,USER_UID: row.USER_UID
                , "_row_status":row_status 
            }, true);
            inlineDialogClose();
        }
        inlineDialog(<CM_2300__사용자조회  onRowDoubleClick= {gridOnRowDoubleClickHandler} />,undefined,false,"sm"); 
    }
    const tmp =(props.p.row  as any)["_row_status"];
    

    return (
        <input
        className="rdg-text-editor"
        readOnly={true} 
        value={props.p.row[props.p.column.key]}
        onBlur={() => props.p.onClose(true)}
        onClick={clickHandler}
      />
    );
}
  