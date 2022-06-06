package com.example.generateuuid;

import dataset.DataRow;
import dataset.DataSet;
import dataset.DataTable;
import java.util.UUID;

import running.common.SAProxy;
import com.github.f4b6a3.uuid.UuidCreator;

public class UuidUtil extends SAProxy {

    public DataSet getOrderedUUID(DataSet InDs,String InDsNames, String outDsNames)  throws Exception {
    	DataSet OUT_DS = new DataSet();
    	DataTable OUT_RSET = OUT_DS.addTable("OUT_RSET");
    	OUT_RSET.addColumn("UUID");
        DataRow drRset = OUT_RSET.addRow();
        drRset.setString("UUID", orderedUUID());
        return OUT_DS;
    }

    private String orderedUUID(){
        UUID uuid = UuidCreator.getTimeOrdered();
        //int variant = uuid.variant();
        //int version = uuid.version();
        //System.out.println("variant:"+variant);
        //System.out.println("version:"+version);
        //System.out.println("uuid:"+uuid.toString());
        return uuid.toString().replaceAll("-", "");

    }
}

/*
https://medium.com/aha-official/%EC%95%84%ED%95%98-rest-api-%EC%84%9C%EB%B2%84-%EA%B0%9C%EB%B0%9C-6-43568d94878a
 Ordered UUID
UUID버전이 V1 V3, V4 ,V5가 존재한다.
V1 버전인경우  조합하면 정렬해서 쓸수있다. 아래는 V1에 대한 내용이다. (V1이 타입스탬프를 기반으로 구해서 가능하다고 한다.)
사실 생성된 UUID 값을 그대로 id 사용하는 것은 적절치 않습니다. UUID 는 보통 다음의 구조를 가집니다
xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
685ef9fd-103b-480b-8be9-906c8d92dae8
https://www.percona.com/blog/2014/12/19/store-uuid-optimized-way/
위의 글을 한줄로 요약하자면 UUID 의 구조를 아래와 같이 변경하면 인덱싱이 가능한, 순서를 ‘어느정도' 보장받는 수 체계로 변환할 수 있다고 합니다.
1-2-3-4-5  ->  32145
a48ebc52–8755–4e74–93e4–44e6c0f2f180
3: 4e74
2: 8755
1: a48ebc52
4: 93e4
5: 44e6c0f2f180
=> 4e748755a48ebc5293e444e6c0f2f180
이렇게 변환해서 pk로 쓰면  uuid가 비교적 순서적으로 값이 나오게 되어 디비에 pk로 지정해도 파편화가 들일어난다고한다.

java uuid v1을 만드는 소스를 가져와야한다.
어느정도만 되었지 정렬이 안된다. 그래서 외부 라이브러리 쓰기로 했다.

https://github.com/f4b6a3/uuid-creator

*/