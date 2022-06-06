import Grid,{GridHandler} from '@/form-components/Grid';
import GridCombo from '@/form-components/GridCombo';
import GridTextEditor from '@/form-components/GridTextEditor';
import  * as pjtUtil  from '@/utils/pjtUtil';
import React, { useState,useEffect,useRef,useContext } from "react";
import send, { send_sql } from '@/utils/send';
import {Stack,Button} from '@mui/material'
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { dark } from 'react-syntax-highlighter/dist/cjs/styles/prism';
import {MenuContext,getBlankRight} from "@/store/MenuStore";
import { useForm } from "react-hook-form";

interface IGridColumn {
    key: string;
    name: string;
    width: number;
    sortable: boolean;
  }


export  const CM_7300__SQL = () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm,inlineDialog } = menuStoreContext;

    const childRef = useRef<HTMLTextAreaElement>(null);
    const childGridRef = useRef<GridHandler>(null);
    useEffect(()=>{
    },[]);


    const sqlGetOutColumnsHandler=(event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
        if(childRef.current){
            const sql = childRef.current.value.trim();
            if(sql===""){
               messageAlert("SQL 내용이 존재하지 않습니다.");
               return; 
            }
           var param = {
               SQL : sql
           }
           const getData = async ()=>{
                const data:any= await send_sql("getOutColumns",param);
                console.log(data)
                

                const txt=(
                    <SyntaxHighlighter language="plaintext" style={dark}>
                        {data.join("\n")}
                    </SyntaxHighlighter>
                )

                inlineDialog(txt);
           }
           getData().then(res=>{
                console.log({res})
            }).catch(err=>{
                console.log({err})
                //여기서 에러 메시지를 처리하자.
                const txt=(
                    <SyntaxHighlighter language="plaintext" style={dark}>
                        {err.data}
                    </SyntaxHighlighter>
                )
                inlineDialog(txt);
            })
            }
    }

    const sqlRunSQLHandler =(event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
        if(childRef.current){
            const sql = childRef.current.value.trim();
            if(sql===""){
               messageAlert("SQL 내용이 존재하지 않습니다.");
               return; 
            }
           var param = {
               SQL : sql
           }
           const getData = async ()=>{
                const data:any= await send_sql("runSQL",param);
                console.log({data})
                if(data.command=="SELECT"){
                    const columns:IGridColumn[] = [];
                    data.fields.forEach((c:any)=>{
                        //console.log(c);
                        columns.push({
                            key: c.name,
                            name: c.name,
                            width: 100,
                            sortable: true
                        })
                    })

                    const grid = (
                        <>
                        <input type="button" value='조회' onClick={() => {
                                                                if(childGridRef.current){
                                                                    console.log(data.rows)
                                                                    childGridRef.current.setData(data.rows)                        
                                                                }
                                                                            
                                                                        }} />
                        <Grid style={{ height: 600, width: '100%' }} columns={columns}  checkbox={false} showRowStatus={false}  ref={childGridRef} />
                        </>
                    )
                    inlineDialog(grid);
                } else if(data.command=="DELETE" ||
                          data.command=="UPDATE" ||
                          data.command=="INSERT" ){
                    const tmp2 = {command:data.command,rowCount:data.rowCount}
                    const txt=(
                        <SyntaxHighlighter language="plaintext" style={dark}>
                            {JSON.stringify(tmp2,null,2)}
                        </SyntaxHighlighter>
                    )
                    inlineDialog(txt);
                }                
            }
            /*https://velog.io/@vraimentres/async-%ED%95%A8%EC%88%98%EC%99%80-try-catch*/
            getData().then(res=>{
                console.log({res})
            }).catch(err=>{
                console.log({err})
                //여기서 에러 메시지를 처리하자.
                const txt=(
                    <SyntaxHighlighter language="plaintext" style={dark}>
                        {err.data}
                    </SyntaxHighlighter>
                )
                inlineDialog(txt);
            })            
        }
    }

    const sqlPrettySQLHandler =(event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
        if(childRef.current){
            let sql = childRef.current.value.trim();
            if(sql===""){
               messageAlert("SQL 내용이 존재하지 않습니다.");
               return; 
            }
            if(false){
                sql=`The BR_CM_GRP_CD_FIND has been successfully executed. ( Total Time : 0.105 sec [Only Server : 0.011 sec] )
                [1] [DA:DA_CM_GRP_CD_findCmGrpCd]
                [SQL Statement]
                SELECT
                GRP_CD_SEQ
                ,GRP_CD
                ,GRP_NM
                ,USE_YN
                ,ORD
                ,RMK
                ,CRT_USR_NO
                ,UPDT_USR_NO 
                ,CRT_DTM
                ,UPDT_DTM
                FROM TB_CM_GRP_CD


                WHERE GRP_CD LIKE '%' || ? || '%'
                ORDER BY ORD ASC
                /* [BizActor].[DA_CM_CD].[DA_CM_GRP_CD_findCmGrpCd] */
                [Parameters Start]
                Param 1 : ORDER
                [Parameters End]
                3 row(s) fetched. 3 row(s) total.
                `;
            }
            var ks = sql.replace(/\r/g, "").split(/\n/);
            var sql_flag=false;
            var parameters_start=false;
            var parameters_end=false;
            var sql_arr=[];
            var where_arr=[];
            for(var i =0;i<ks.length;i++){
                var tmp = ks[i].trim();
                if(tmp!=''){
                    if(tmp.indexOf('[Parameters End]')>=0){
                        parameters_end=true;
                    }
                    /*where 본문*/
                    if(parameters_start==true  && parameters_end==false){
                        if(tmp.indexOf(':')>0){
                            //Param 15 : 
                            var tmp2=tmp.substring(tmp.indexOf(':')+1)
                            where_arr.push(tmp2.trim());
                        }
                    }
                    /*where 본문*/
                    if(tmp.indexOf('[Parameters Start]')>=0){
                        parameters_start=true;
                    }
                    /*sql본문*/
                    if(sql_flag==true && parameters_start==false){
                        sql_arr.push(tmp.trim());
                    }
                    /*sql본문*/
                    if(tmp.indexOf('[SQL Statement]')>=0){
                        sql_flag=true;
                    }
                }
            }
            if(sql_flag==false){
                messageAlert("[SQL Statement] 구분자가 sql문에 없어요");
                return; 
            }
            if(parameters_start==false){
                messageAlert("[Parameters Start] 구분자가 sql문에 없어요");
                return; 
            }
            if(parameters_end==false){
                messageAlert("[Parameters End] 구분자가 sql문에 없어요");
                return; 
            }
            var sql_final=sql_arr.join('\n');
            for(var i =0;i<where_arr.length;i++){
                sql_final=sql_final.replace("?",     "'"+pjtUtil._escapeString(where_arr[i])+"'"         );
            }
           const txt=(
               <SyntaxHighlighter language="sql" style={dark}>
                   {sql_final}
               </SyntaxHighlighter>
           )
           inlineDialog(txt);
        }
    }

    const sqBindingSqlHandler = (event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
        if(childRef.current){
            let sql = childRef.current.value.trim();
            if(sql===""){
               messageAlert("SQL 내용이 존재하지 않습니다.");
               return; 
            }
            if(false){
                sql=`The BR_CM_GRP_CD_FIND has been successfully executed. ( Total Time : 0.105 sec [Only Server : 0.011 sec] )
                [1] [DA:DA_CM_GRP_CD_findCmGrpCd]
                [SQL Statement]
                SELECT
                GRP_CD_SEQ
                ,GRP_CD
                ,GRP_NM
                ,USE_YN
                ,ORD
                ,RMK
                ,CRT_USR_NO
                ,UPDT_USR_NO 
                ,CRT_DTM
                ,UPDT_DTM
                FROM TB_CM_GRP_CD


                WHERE GRP_CD LIKE '%' || ? || '%'
                ORDER BY ORD ASC
                /* [BizActor].[DA_CM_CD].[DA_CM_GRP_CD_findCmGrpCd] */
                [Parameters Start]
                Param 1 : ORDER
                [Parameters End]
                3 row(s) fetched. 3 row(s) total.
                `;
            }
            var ks = sql.replace(/\r/g, "").split(/\n/);
            var sql_flag=false;
            var parameters_start=false;
            var parameters_end=false;
            var sql_arr=[];
            var where_arr=[];
            for(var i =0;i<ks.length;i++){
                var tmp = ks[i].trim();
                if(tmp!=''){
                    if(tmp.indexOf('[Parameters End]')>=0){
                        parameters_end=true;
                    }
                    /*where 본문*/
                    if(parameters_start==true  && parameters_end==false){
                        if(tmp.indexOf(':')>0){
                            //Param 15 : 
                            var tmp2=tmp.substring(tmp.indexOf(':')+1)
                            where_arr.push(tmp2.trim());
                        }
                    }
                    /*where 본문*/
                    if(tmp.indexOf('[Parameters Start]')>=0){
                        parameters_start=true;
                    }
                    /*sql본문*/
                    if(sql_flag==true && parameters_start==false){
                        sql_arr.push(tmp.trim());
                    }
                    /*sql본문*/
                    if(tmp.indexOf('[SQL Statement]')>=0){
                        sql_flag=true;
                    }
                }
            }
            if(sql_flag==false){
                messageAlert("[SQL Statement] 구분자가 sql문에 없어요");
                return; 
            }
            if(parameters_start==false){
                messageAlert("[Parameters Start] 구분자가 sql문에 없어요");
                return; 
            }
            if(parameters_end==false){
                messageAlert("[Parameters End] 구분자가 sql문에 없어요");
                return; 
            }
            var sql_final=sql_arr.join('\n');
            for(var i =0;i<where_arr.length;i++){
                sql_final=sql_final.replace("?",     "'"+pjtUtil._escapeString(where_arr[i])+"'"         );
            }
           const txt=(
               <SyntaxHighlighter language="sql" style={dark}>
                   {sql_final}
               </SyntaxHighlighter>
           )
           inlineDialog(txt);
        }
    }

    const sqBindingSqlFailHandler = (event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
        if(childRef.current){
            let sql = childRef.current.value.trim();
            if(sql===""){
               messageAlert("SQL 내용이 존재하지 않습니다.");
               return; 
            }
            if(false){
                sql=`--------------------------------------------------------------------------------
EXCEPTION_MESSAGE
--------------------------------------------------------------------------------
ERROR: syntax error at or near "1"
Position: 118

--------------------------------------------------------------------------------
LOC
--------------------------------------------------------------------------------
[DA:DA_CM_GRP_CD_findCmGrpCd:EXEC-QRY]

--------------------------------------------------------------------------------
DATA
--------------------------------------------------------------------------------
Query : SELECT
GRP_CD_SEQ
,GRP_CD
,GRP_NM
,USE_YN
,ORD
,RMK
,CRT_USR_NO
,UPDT_USR_NO 
,CRT_DTM
,UPDT_DTM
FROM TB_CM_GRP_CD

1=1
WHERE GRP_CD LIKE '%' || ? || '%'
ORDER BY ORD ASC
/* [BizActor].[DA_CM_CD].[DA_CM_GRP_CD_findCmGrpCd_MOD0123456789MOD_] */
Para : Param 1 : ORDER


--------------------------------------------------------------------------------
TYPE
--------------------------------------------------------------------------------
LOGIC

--------------------------------------------------------------------------------
STACK_TRACE
--------------------------------------------------------------------------------
running.exception.RunningServerException: ERROR: syntax error at or near "1"
Position: 118
at running.component.DAManager.a(Unknown Source)
at running.component.DAManager.a(Unknown Source)
at running.component.DAManager.OperateDM(Unknown Source)
at running.component.SvcManager.ExecuteServiceForTest(Unknown Source)
at running.server.TestSyncServer.ExecuteServiceForTest(Unknown Source)
at modeling.server.ModelServer.a(Unknown Source)
at modeling.server.ModelServer.requestService(Unknown Source)
at bizactor.modeling.ModelingServlet.doPost(Unknown Source)
at javax.servlet.http.HttpServlet.service(HttpServlet.java:660)
at javax.servlet.http.HttpServlet.service(HttpServlet.java:741)
at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:231)
at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)
at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:52)
at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)
at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)
at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:199)
at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:96)
at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:528)
at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:139)
at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:81)
at org.apache.catalina.valves.AbstractAccessLogValve.invoke(AbstractAccessLogValve.java:678)
at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:87)
at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:343)
at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:609)
at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:65)
at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:810)
at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1506)
at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49)
at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
at java.lang.Thread.run(Thread.java:748)


--------------------------------------------------------------------------------
ETYPE
--------------------------------------------------------------------------------
JAVA
                `;
            }
            var ks = sql.replace(/\r/g, "").split(/\n/);
            var sql_flag=false;
            var parameters_start=false;
            var parameters_end=false;
            var sql_arr=[];
            var where_arr=[];
            var idx=0;
            for(var i =0;i<ks.length;i++){
                var tmp = ks[i].trim();
                if(tmp!=''){
                    if(parameters_start==true  && tmp.indexOf('--------------------------------------------------------------------------------')>=0){
                        parameters_end=true;
                    }
                    /*where 본문*/
                    if(tmp.indexOf('Para :')>=0){
                        parameters_start=true;
                    }
                    if(parameters_start==true  && parameters_end==false){
                        if(tmp.indexOf('Para :')>=0){
                            var tmp2=tmp.substring(tmp.indexOf('Para :')+("Para :".length)+1)
                            console.log("tmp2=>"+tmp2);

                            if(tmp2.indexOf(':')>0){
                                //Param 15 : 
                                var tmp3=tmp2.substring(tmp2.indexOf(':')+1)
                                where_arr.push(tmp3.trim());
                            }
                        } else {
                            if(tmp.indexOf(':')>0){
                                //Param 15 : 
                                var tmp2=tmp.substring(tmp.indexOf(':')+1)
                                where_arr.push(tmp2.trim());
                            }
                        }
                    }

                    /*sql본문*/
                    if(tmp.indexOf('Query :')>=0){
                        sql_flag=true;
                    }
                    /*sql본문*/
                    if(sql_flag==true && parameters_start==false){
                        if(tmp.indexOf('Query :')>=0){
                            var tmp = tmp.split(':')[1];
                            sql_arr.push(tmp.trim());
                        } else {
                            sql_arr.push(tmp.trim());
                        }
                        
                    }

                }
            }
            
            console.log(sql_arr);
            console.log(where_arr);

            if(sql_flag==false){
                messageAlert("Query : 구분자가 sql문에 없어요");
                return; 
            }
            if(parameters_start==false){
                messageAlert("Para : 구분자가 sql문에 없어요");
                return; 
            }
            if(parameters_end==false){
                messageAlert("---- 구분자가 sql문에 없어요");
                return; 
            }


            var sql_final=sql_arr.join('\n');
            for(var i =0;i<where_arr.length;i++){
                sql_final=sql_final.replace("?",     "'"+pjtUtil._escapeString(where_arr[i])+"'"         );
            }
           const txt=(
               <SyntaxHighlighter language="sql" style={dark}>
                   {sql_final}
               </SyntaxHighlighter>
           )
           inlineDialog(txt);
        }
    }
    //text-area 대신에 아래걸 붙이자.
    //https://ace.c9.io/
    //https://github.com/securingsincity/react-ace

    function onChange(newValue:any) {
        console.log("change", newValue);
    }
      

    return (
            <>
            * 

            <Stack
                mt={2}
                direction="row"
                justifyContent="flex-start"
                alignItems="flex-start"
                spacing={0.5}
                >
                    <Button variant="contained" color="success"  onClick={sqlGetOutColumnsHandler}>getOutColumns</Button>          
                    <Button variant="contained" color="success"  onClick={sqlRunSQLHandler}>SQL실행</Button>          
                    <Button variant="contained" color="success"  onClick={sqlPrettySQLHandler}>SQL정렬</Button>          
                    <Button variant="contained" color="success"  onClick={sqBindingSqlHandler}>SQL-Ok-바인딩</Button>          
                    <Button variant="contained" color="success"  onClick={sqBindingSqlFailHandler}>SQL-Nok-바인딩</Button>                                                            
            </Stack>
            <textarea className="form-control" rows={40} data-ax-path="INPUT"  name="INPUT" style={{width:'700px'}} ref={childRef} ></textarea>
            </>
    )
}

