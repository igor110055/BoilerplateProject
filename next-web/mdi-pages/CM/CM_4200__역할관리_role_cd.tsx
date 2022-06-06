import Grid,{GridHandler,Maybe} from '@/form-components/Grid';
import GridCombo from '@/form-components/GridCombo';
import GridTextEditor from '@/form-components/GridTextEditor';
import { useForm } from "react-hook-form";
import {FormSelect}  from '@/form-components/FormSelect';
import {FormTextFiled} from '@/form-components/FormTextFiled';
import React, { useState,useEffect,useRef,useReducer,useContext } from "react";
import send from '@/utils/send';
import {Stack,Button} from '@mui/material'
import {MenuContext} from "@/store/MenuStore";
import { CalculatedColumn } from 'react-data-grid';

interface IPageProps {
    onRowClick?: Maybe<(row: any, column: CalculatedColumn<any, any>) => void>;
}

interface IFormInput {
}

const defaultValues = {

};

export  const CM_4200__역할관리_role_cd = (props:IPageProps) => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm } = menuStoreContext;

    const childRef = useRef<GridHandler>(null);
    const [useYnData,setUseYnData] = useState([]);
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError } = methods;
    const fnSearch= async (data: IFormInput) => {
        if(childRef.current){
            childRef.current.loadData({
                                        BR:"BR_CM_ROLE_CD_FIND",
                                        PARAM: {
                                            brRq : 'IN_DATA'
                                            ,brRs : 'OUT_DATA'
                                            ,IN_DATA:[data]
                                        }
                                    });

        }
    }
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
            setUseYnData(tmp)
        }
        getData();
        handleSubmit(fnSearch)()
    },[]);

    const columns = [
        { key: 'ROLE_CD', name: '역할코드', width: 130 ,  sortable: true ,editor: GridTextEditor},
        { key: 'ROLE_NM', name: '역할명', width: 140 ,editor: GridTextEditor},
        { key: "USE_YN", name: '사용여부', width: 80 ,  sortable: true  ,editor: (p:any) => (<GridCombo p={p} options={useYnData}  />) , editorOptions: {editOnClick: true}},
        { key: 'ORD', name: '정렬', width: 100 ,  sortable: true  ,editor: GridTextEditor},
        { key: 'RMK', name: '비고', width: 100 ,editor: GridTextEditor},            
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
                send('BR_CM_PGM_SAVE',{
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
                send('BR_CM_PGM_RM',{
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
            <Stack mt={2} direction="row" justifyContent="flex-start" alignItems="flex-start" spacing={0.5}>
                    <Button variant="contained" color="success"   onClick={handleSubmit(fnSearch)}>조회</Button>
                    <Button variant="contained" color="success" onClick={() => {if(childRef.current){const tmp = childRef.current.addRow();}}}>추가</Button>
                    <Button variant="contained" color="success"  onClick={saveHandler}>저장</Button>                                                                        
                    <Button variant="contained" color="success"  onClick={delHandler}>삭제</Button>                                                                        
            </Stack>
            <Grid style={{ height: 200, width: '100%' }} columns={columns}  checkbox={true} showRowStatus={true} ref={childRef}  onRowClick={props.onRowClick}  />
            </>
    )
  }
  
  

