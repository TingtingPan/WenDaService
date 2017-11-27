<#include "header.ftl">
<link rel="stylesheet" href="../styles/letter.css">

<div id="main">
    <div class="zg-wrap zu-main clearfix ">
        <ul class="letter-chatlist">
        <#--#foreach($message in $messages)-->
        <#list messages as message>
            <li id="msg-item-4009580">
                <a class="list-head">
                    <img alt="头像" src="${message.headUrl}">
                </a>
                <div class="tooltip fade right in">
                    <div class="tooltip-arrow"></div>
                    <div class="tooltip-inner letter-chat clearfix">
                        <div class="letter-info">
                            <p class="letter-time">
                                <#--$date.format('yyyy-MM-dd HH:mm:ss',-->
                                <#--$!message.message.createdDate)-->
                                <#if message.message.createdDate??>
                                  ${message.message.createdDate?string("yyyy-MM-dd HH:mm:ss")}
                                <#else >
                                    2017-11-24 21:02
                                </#if>
                            </p>
                            <!-- <a href="javascript:void(0);" id="del-link" name="4009580">删除</a> -->
                        </div>
                        <p class="chat-content">
                            ${message.message.content}
                        </p>
                    </div>
                </div>
            </li>
        </#list>
        </ul>

    </div>
</div>


<#include "footer.ftl">
