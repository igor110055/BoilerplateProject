import Grid,{GridHandler} from '@/form-components/Grid';
import {FormSelect}  from '@/form-components/FormSelect';
import React, { useState,useEffect,useRef,useContext } from "react";
import send from '@/utils/send';
import {Stack,Button} from '@mui/material'
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { dark } from 'react-syntax-highlighter/dist/cjs/styles/prism';
import {MenuContext,getBlankRight} from "@/store/MenuStore";
import { useForm } from "react-hook-form";
import GridTextEditor from '@/form-components/GridTextEditor';


interface IFormInput {
    TABLE_NAME: string;
  }

const defaultValues = {
    TABLE_NAME: "",
};

export  const CM_7200__컬럼상세 = () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm,inlineDialog } = menuStoreContext;

    const childRef = useRef<GridHandler>(null);
    const [tableNameData,setTableNameData] = useState([]);
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError } = methods;
    const fnSearch= async (data: IFormInput) => {
        if(childRef.current){
            childRef.current.loadData({
                                        BR:"BR_CM_DB_POSTGRESQL_retrieveColumnList",
                                        PARAM: {
                                            brRq : 'IN_PSET'
                                            ,brRs : 'OUT_RSET'
                                            ,IN_PSET:[data]
                                        }
                                    });

        }
    }
    useEffect(()=>{
        const getData = async ()=>{
            const data:any= await send("BR_CM_DB_POSTGRESQL_retrieveTableList", {
                 brRq: ''
                ,brRs: 'OUT_RSET'
            });

            const tmp= data.OUT_RSET.map((m:any)=>{
                return {
                    value: m["TABLE_NAME"],
                    text: m["COMBO_TEXT"]            
                };
            })
            setTableNameData(tmp)
        }
        getData();
        handleSubmit(fnSearch)()
    },[]);

    const columns = [
        { key: 'RNK', name: 'RNK', width: 50 , minWidth:50,  sortable: true},
        { key: 'TABLE_NAME', name: 'TABLE_NAME', width: 240},
        { key: "TABLE_COMMENT", name: 'TABLE_COMMENT', width: 200 ,  sortable: true},
        { key: "COLUMN_NAME", name: 'COLUMN_NAME', width: 200 ,  sortable: true},
        { key: "DATA_TYPE", name: 'DATA_TYPE', width: 200 ,  sortable: true},
        { key: "C_SIZE", name: 'CHR_MAX_LEN', width: 120 ,  sortable: true},
        { key: "NULL", name: 'IS_NULL', width: 100 ,  sortable: true},
        { key: "COLUMN_COMMENT", name: 'COLUMN_COMMENT', width: 200 ,  sortable: true  ,editor: GridTextEditor},
        { key: "ORD", name: 'ORD', width: 100 ,  sortable: true},
    ];


    
    const saveHandler=(event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
        if(childRef.current){
            const data = childRef.current.getModifiedRows();
            var updt_cnt= data.updatedRows.length;
            if((updt_cnt)==0) {
                messageAlert("저장할 내용이 존재하지 않습니다.");
                return;
            }
            messageConfirm("컬럼코멘트를 저장하시겠습니까?",function()  {
                //_this.showProgress();	
                send('BR_CM_DB_POSTGRESQL_saveColumnCmt',{
                    brRq 		: 'UPDT_DATA'
                    ,brRs 		: ''
                    ,UPDT_DATA	: data.updatedRows
                }).then(function(data){
                    //_this.hideProgress();
                    if(data){
                        messageAlert("테이블코멘트를 저장하였습니다.",function()  {
                            handleSubmit(fnSearch)()
                        });
                    }
                })                      
            })
        }
    }

    
    const dropColumnHandler=(event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
        if(childRef.current){
            const data = childRef.current.getCheckedData();
            if(data.length<=0) {
                messageAlert('선택된 항목이 없습니다.');
                return;
            }
            messageConfirm("Column을 Drop 하시겠습니까?",function()  {
                //_this.showProgress();	
                send('BR_CM_DB_POSTGRESQL_dropColumn',{
                    brRq 		: 'IN_PSET'
                    ,brRs 		: ''
                    ,IN_PSET	: data
                }).then(function(data){
                    //_this.hideProgress();
                    if(data){
                        messageAlert("Column을 Drop 하였습니다.",function()  {
                            handleSubmit(fnSearch)()
                        });
                    }
                })                      
            })
        }      
    }

    const sqlSelectHandler=(event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
        if(childRef.current){
            const data = childRef.current.getCheckedData();
            console.log({data});
            if(data.length<=0) {
                messageAlert('선택된 항목이 없습니다.');
                return;
            }

            let table_name = data[0].TABLE_NAME;
            let sql_select ='SELECT '+'\n' ;
            for(var i=0;i<data.length;i++){
                if(i==0){
                    sql_select = sql_select+'    A.'+data[i].COLUMN_NAME+'\n' ;
                } else {
                    sql_select = sql_select+'    ,A.'+data[i].COLUMN_NAME+'\n' ;
                }                        
            }
            sql_select = sql_select+'FROM '+table_name+' A'+'\n' ;
            console.log(sql_select);
            const sql=(
                <SyntaxHighlighter language="sql" style={dark}>
                    {sql_select}
                </SyntaxHighlighter>
            )
            inlineDialog(sql);
            return;
        }      
    }

    const sqlInsertHandler=(event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
        if(childRef.current){
            const data = childRef.current.getCheckedData();
            console.log({data});
            if(data.length<=0) {
                messageAlert('선택된 항목이 없습니다.');
                return;
            }

            let table_name = data[0].TABLE_NAME;
            let sql_insert ='INSERT INTO '+table_name +'\n' ;
            sql_insert =sql_insert +'('+'\n' ;
            for(var i=0;i<data.length;i++){
                if(i==0){
                    sql_insert = sql_insert+' '+data[i].COLUMN_NAME+'\n' ;
                } else {
                    sql_insert = sql_insert+', '+data[i].COLUMN_NAME+'\n' ;
                }                        
            }
            sql_insert =sql_insert+')' +'\n' ;
            sql_insert =sql_insert+'VALUES' +'\n' ;
            sql_insert =sql_insert+'( '+'\n' ;
            for(var i=0;i<data.length;i++){
                if(i==0){
                    sql_insert = sql_insert+' @'+data[i].COLUMN_NAME+'\n' ;
                } else {
                    sql_insert = sql_insert+', @'+data[i].COLUMN_NAME+'\n' ;
                }                        
            }
            sql_insert =sql_insert+')' ;
            const sql=(
                <SyntaxHighlighter language="sql" style={dark}>
                    {sql_insert}
                </SyntaxHighlighter>
            )
            inlineDialog(sql);
            return;
        }      
    }

    const sqlUpdateHandler=(event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
        if(childRef.current){
            const data = childRef.current.getCheckedData();
            console.log({data});
            if(data.length<=0) {
                messageAlert('선택된 항목이 없습니다.');
                return;
            }

            let table_name = data[0].TABLE_NAME;
            let sql_update ='UPDATE '+table_name+'\n' ;
            sql_update = sql_update+'   SET \n';
            var len=0;
            for(var i=0;i<data.length;i++){
                if(len<=data[i].COLUMN_NAME.length){
                    len=data[i].COLUMN_NAME.length;
                }
            }
            len=len+1;

            for(var i=0;i<data.length;i++){
                if(i==0){
                    sql_update = sql_update+'        '+getBlankRight(data[i].COLUMN_NAME,len)+'=@'+data[i].COLUMN_NAME+'\n' ;
                } else {
                    sql_update = sql_update+'       ,'+getBlankRight(data[i].COLUMN_NAME,len)+'=@'+data[i].COLUMN_NAME+'\n' ;
                }                        
            }
            sql_update =sql_update+' WHERE 1=1 ' ;
            const sql=(
                <SyntaxHighlighter language="sql" style={dark}>
                    {sql_update}
                </SyntaxHighlighter>
            )
            inlineDialog(sql);
            return;
        }      
    }

    const sqlDelteHandler=(event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
        if(childRef.current){
            const data = childRef.current.getCheckedData();
            console.log({data});
            if(data.length<=0) {
                messageAlert('선택된 항목이 없습니다.');
                return;
            }

            let table_name = data[0].TABLE_NAME;
            let sql_delete ='DELETE FROM '+table_name+'\n' ;
            sql_delete = sql_delete+'WHERE 1=1 \n' ;
            for(var i=0;i<data.length;i++){
                sql_delete = sql_delete+'  AND '+data[i].COLUMN_NAME+'=@'+data[i].COLUMN_NAME +'\n' ;
            }
            const sql=(
                <SyntaxHighlighter language="sql" style={dark}>
                    {sql_delete}
                </SyntaxHighlighter>
            )
            inlineDialog(sql);
            return;
        }      
    }

    const dataColumnCopyHandler=(event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
        if(childRef.current){
            const data = childRef.current.getCheckedData();
            console.log({data});
            if(data.length<=0) {
                messageAlert('선택된 항목이 없습니다.');
                return;
            }
            let dataColumn="";
            for(var i=0;i<data.length;i++){
                dataColumn = dataColumn+''+data[i].COLUMN_NAME+'\t'+'String'+'\t'+''+'\t'+(data[i].COLUMN_COMMENT==null ? "": data[i].COLUMN_COMMENT==null)+'\n' ;
            }
            const txt=(
                <SyntaxHighlighter language="plaintext" style={dark}>
                    {dataColumn}
                </SyntaxHighlighter>
            )
            inlineDialog(txt);
            return;
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
                    <FormSelect name="TABLE_NAME" control={control} label="테이블선택" sx={{width:400}} rules={{ required: false }}  options={tableNameData}  />
                    <Button variant="contained" color="success"   onClick={handleSubmit(fnSearch)}>조회</Button>
                    <Button variant="contained" color="success"  onClick={saveHandler}>저장</Button>                                                                        
                    <Button variant="contained" color="success"  onClick={dropColumnHandler}>컬럼삭제</Button>                                                                        
                    <Button variant="contained" color="success"  onClick={sqlSelectHandler}>SELECT문생성</Button>                                                                        
                    <Button variant="contained" color="success"  onClick={sqlInsertHandler}>INSERT문생성</Button>                                                                        
                    <Button variant="contained" color="success"  onClick={sqlUpdateHandler}>UPDATE문생성</Button>                                                                        
                    <Button variant="contained" color="success"  onClick={sqlDelteHandler}>DELETE문생성</Button>                                                                        
                    <Button variant="contained" color="success"  onClick={dataColumnCopyHandler}>DataColumn복사</Button>                                                                        
                    
            </Stack>
            <Grid style={{ height: 600, width: '100%' }} columns={columns}  checkbox={true} showRowStatus={true} ref={childRef} />
            </>
    )
  }

