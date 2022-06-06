import Grid,{GridHandler,Maybe} from '@/form-components/Grid';
import GridTextEditor from '@/form-components/GridTextEditor';
import { useForm } from "react-hook-form";
import {FormSelect}  from '@/form-components/FormSelect';
import React, { DetailedHTMLProps, InputHTMLAttributes, useContext, useState,useEffect,useRef }  from "react";
import send from '@/utils/send';
import {Stack,Button} from '@mui/material'
import {MenuContext} from "@/store/MenuStore";
import { CalculatedColumn } from 'react-data-grid';
import { CM_1150__프로그램조회팝업 } from './CM_1150__프로그램조회팝업';

interface IPageProps {
    onRowClick?: Maybe<(row: any, column: CalculatedColumn<any, any>) => void>;
}

interface IFormInput {
    PRNT_MENU_CD: string;
  }

const defaultValues = {
    PRNT_MENU_CD: "",
};

export  const CM_1300__메뉴관리_sub1 = (props:IPageProps) => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm } = menuStoreContext;

    const childRef = useRef<GridHandler>(null);
    const [ctgData,setCtgData] = useState([]);
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError } = methods;
    const fnSearch= async (data: IFormInput) => {
        if(childRef.current){
            childRef.current.loadData({
                                        BR:"BR_CM_MENU_FIND",
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
            const data:any= await send("BR_CM_MENU_FIND", {brRq: 'IN_DATA'
            ,brRs: 'OUT_DATA'
            ,IN_DATA: [  { MENU_KIND : 'M'}]
            });

            const tmp= data.OUT_DATA.map((m:any)=>{
                return {
                    value: m["MENU_CD"],
                    text: m["MENU_NM"]            
                };
            })
            setCtgData(tmp)
        }
        getData();
        handleSubmit(fnSearch)()
    },[]);

    const columns = [
        { key: 'MENU_CD', name: '메뉴코드', width: 150,  sortable: true ,editor: GridTextEditor},            
        { key: 'MENU_NM', name: '메뉴명', width: 200,  sortable: true ,editor: GridTextEditor},            
        { key: 'PGM_ID', name: '프로그램ID', width: 120,  sortable: true ,editor: (p:any) =>(<GridPopup p={p}   />) ,editorOptions: {editOnClick: true} },
        { key: 'MENU_PATH', name: 'PATH', width: 400,  sortable: true ,editor: GridTextEditor},            
        { key: 'PRNT_MENU_CD', name: '부모메뉴', width: 200,  sortable: true ,editor: GridTextEditor},            
        { key: 'RMK', name: '비고', width: 80,  sortable: true ,editor: GridTextEditor},            
        { key: "MENU_KIND", name: '종류', width: 60 ,minWidth:60,  sortable: true ,editor: GridTextEditor},            
        { key: 'MENU_LVL', name: 'LVL', width: 60,minWidth:60 ,  sortable: true ,editor: GridTextEditor},            
        { key: 'ORD', name: '정렬', width: 80,  sortable: true ,editor: GridTextEditor},            
        { key: 'MENU_NO', name: '번호', width: 60,minWidth:60 ,  sortable: true},                    
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
                send('BR_CM_MENU_SAVE',{
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
                send('BR_CM_MENU_RM',{
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

    const gridOnRowClickHandler  = (row: any, column: CalculatedColumn<any, any>)=>{
        console.log(row);               
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
                    <FormSelect name="PRNT_MENU_CD" control={control} label="부모메뉴코드" sx={{width:200}} rules={{ required: false }}  options={ctgData}  />
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
            <Grid style={{ height: 600, width: '100%' }} columns={columns}  checkbox={true} showRowStatus={true} ref={childRef} onRowClick={props.onRowClick} />
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
            props.p.onRowChange({ ...props.p.row, [props.p.column.key]: row[props.p.column.key] , "_row_status":row_status }, true);
            inlineDialogClose();
        }
        inlineDialog(<CM_1150__프로그램조회팝업  onRowDoubleClick= {gridOnRowDoubleClickHandler} />,undefined,false,"sm"); 
    }

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