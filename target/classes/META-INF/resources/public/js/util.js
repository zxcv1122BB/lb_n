/**
 * Created by ASUS on 2017/9/20.
 */
var util = {
   getJsonTree : function(data,parentId){
    var itemArr=[];
    for(var i=0;i<data.length;i++){
        var node=data[i];
        //data.splice(i, 1)
        if(node.pId==parentId ){
            var newNode={id:node.id,title:node.title,href:node.href,children:this.getJsonTree(data,node.id)};
            itemArr.push(newNode);
        }
    }
    return itemArr;
}

};
