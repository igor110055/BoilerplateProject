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
import { CalculatedColumn } from 'react-data-grid';
import { CM_2300__사용자조회 } from '@/mdi-pages/CM/CM_2300__사용자조회';


interface CM_4200__역할관리_user_role_cd_sub2Props {
}
   

export type CM_4200__역할관리_user_role_cd_sub2Handler = {
    fnSearchGrid: (GRP_CD:string)=>void;
    
};

interface IFormInput {
    ROLE_CD:string;
}

const defaultValues = {
    ROLE_CD:""
};

export  const CM_4200__역할관리_user_role_cd  = forwardRef<CM_4200__역할관리_user_role_cd_sub2Handler, CM_4200__역할관리_user_role_cd_sub2Props>((props, ref)=> {
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm } = menuStoreContext;

    const childRef = useRef<GridHandler>(null);
    const [useYnData,setUseYnData] = useState([]);
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError,register,setValue } = methods;
    const fnSearch= async (data: IFormInput) => {
        if(childRef.current){
            childRef.current.loadData({
                                        BR:"BR_CM_USER_ROLE_CD_FIND",
                                        PARAM: {
                                            brRq : 'IN_DATA'
                                            ,brRs : 'OUT_DATA'
                                            ,IN_DATA: [data]
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

       
    useImperativeHandle(ref, () => ({
            fnSearchGrid(roleCd:string) {
                setValue("ROLE_CD",roleCd)
                handleSubmit(fnSearch)()
            } 
    })
    );

    const columns = [
        { key: 'ROLE_CD', name: '역할코드', width: 140 ,  sortable: true ,editor: GridTextEditor},            
        { key: 'USER_UID', name: '사용자UID', width: 100},
        { key: "USER_NM", name: '사용자명', width: 80 ,  sortable: true  ,editor: (p:any) =>(<GridPopup p={p}   />) ,editorOptions: {editOnClick: true} },
        { key: 'USER_ID', name: '사용자ID', width: 200 ,  sortable: true },
        { key: 'CRT_USER_UID', name: '생성자UID', width: 160 },
        { key: 'CRT_DTM', name: '생성일', width: 160 },
    ];
    
    const saveHandler=(event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
        if(childRef.current){
            const data = childRef.current.getModifiedRows();
            var crt_cnt	= data.createdRows.length;
            var updt_cnt= data.updatedRows.length;
            if((crt_cnt+updt_cnt)==0) {
                messageAlert("사용자 역할 추가에 저장할 내용이 존재하지 않습니다.");
                return;
            }
            messageConfirm("역할-사용자매핑을 저장하시겠습니까?",function()  {
                //_this.showProgress();	
                send('BR_CM_USER_ROLE_CD_SAVE',{
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
            messageConfirm("사용자 역할을 삭제하시겠습니까?",function()  {
                //_this.showProgress();	
                send('BR_CM_USER_ROLE_CD_RM',{
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
                    <input type="text"  {...register("ROLE_CD")} style={{display:"none"}}  />
                    <Button variant="contained" color="success"   onClick={handleSubmit(fnSearch)}>조회</Button>
                    <Button variant="contained" color="success" onClick={() => {if(childRef.current){const tmp = childRef.current.addRow();}}}>추가</Button>
                    <Button variant="contained" color="success"  onClick={saveHandler}>저장</Button>                                                                        
                    <Button variant="contained" color="success"  onClick={delHandler}>삭제</Button>                                                                        
            </Stack>
            <Grid style={{ height: 200, width: '100%' }} columns={columns}  checkbox={true} showRowStatus={true} ref={childRef} />
            </>
    )
  }
)

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
                ,USER_ID: row.USER_ID
                ,USER_UID: row.USER_UID
                ,USER_NM: row.USER_NM
                , "_row_status":row_status 
            }, true);
            inlineDialogClose();
        }
        inlineDialog(<CM_2300__사용자조회  onRowDoubleClick= {gridOnRowDoubleClickHandler} />,undefined,false,"sm"); 
    }
    const tmp =(props.p.row  as any)["_row_status"];
    if(tmp!="C"){
        return <>수정불가</>
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
  

