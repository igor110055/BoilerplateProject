export const removeComma = (str:string):number =>{
    var tmp = String(str);
    var n = parseInt(tmp.replace(/,/g,""));
    return n;
}

export const isTelNo = (strTelNum:string):boolean =>{
	var regExp = /^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})-?[0-9]{3,4}-?[0-9]{4}$/;
    if( !regExp.test(strTelNum)) {
        return false;
    } else {
        return true;
    }
}

export const isEmail = (strEmail:string):boolean =>{
    var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    if (strEmail == '' || !re.test(strEmail)) {
        
        return false;
    } else {
        return true;
    }

}

export const  isEmpty=(param:string):boolean => {
    if(null===param){
        return true; 
    }
    if(undefined===param){
        return true; 
    }
    if(param.length==0){
        return true;
    }
    return false;
}

export const makeUUID = ():string =>{ // UUID v4 generator in JavaScript (RFC4122 compliant)
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 3 | 8);
        return v.toString(16);
    });
}

export const _escapeString = (val:string) => {
    //https://stackoverflow.com/questions/7744912/making-a-javascript-string-sql-friendly
    var regex = new RegExp(/[\0\x08\x09\x1a\n\r"'\\\%]/g)
    var escaper = function escaper(char:string){
        var m = ['\\0', '\\x08', '\\x09', '\\x1a', '\\n', '\\r', "'", '"', "\\", '\\\\', "%"];
        var r = ['\\\\0', '\\\\b', '\\\\t', '\\\\z', '\\\\n', '\\\\r', "''", '""', '\\\\', '\\\\\\\\', '\\%'];
        return r[m.indexOf(char)];
    };
    //Implementation
    var val=val.replace(regex, escaper);     
    console.log(val);

    return val;
};


export const  numberComma=(tmp:string)=>{
    if(isNumeric(tmp)) {
        //tmp = String(tmp);
        //var tmp2 = tmp.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
      var parts = tmp.toString().split("."); 
      return parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",") + (parts[1] ? "." + parts[1] : ""); 
    } else {
        return tmp;
    }
  }

  export const  isNumeric=(num:string, opt?:string)=>{
    //출처: https://sometimes-n.tistory.com/34 [종종 올리는 블로그]
  // 좌우 trim(공백제거)을 해준다.
  num = String(num).replace(/^\s+|\s+$/g, "");
 
  if(typeof opt == "undefined" || opt == "1"){
    // 모든 10진수 (부호 선택, 자릿수구분기호 선택, 소수점 선택)
    var regex = /^[+\-]?(([1-9][0-9]{0,2}(,[0-9]{3})*)|[0-9]+){1}(\.[0-9]+)?$/g;
  }else if(opt == "2"){
    // 부호 미사용, 자릿수구분기호 선택, 소수점 선택
    var regex = /^(([1-9][0-9]{0,2}(,[0-9]{3})*)|[0-9]+){1}(\.[0-9]+)?$/g;
  }else if(opt == "3"){
    // 부호 미사용, 자릿수구분기호 미사용, 소수점 선택
    var regex = /^[0-9]+(\.[0-9]+)?$/g;
  }else{
    // only 숫자만(부호 미사용, 자릿수구분기호 미사용, 소수점 미사용)
    var regex = /^[0-9]$/g;
  }
 
  if( regex.test(num) ){
    const tmp:any = num.replace(/,/g, "");
    return isNaN(tmp) ? false : true;
  }else{ return false;  }
}  
