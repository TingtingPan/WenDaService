<#include "header.ftl">
<link rel="stylesheet" href="../styles/letter.css">
<div id="main">
    <div class="zg-wrap zu-main clearfix ">
        <ul class="letter-list">
        <#list conversations as conversation>
            <li id="conversation-item-10001_622873">
                <a class="letter-link" href="/msg/detail?conversationId=$conversation.conversation.conversationId">
                </a>
                <div class="letter-info">
                    <#--<span class="l-time">$date.format('yyyy-MM-dd HH:mm:ss', $conversation.conversation.createdDate)</span>-->
                        <#if conversation.conversation.createdDate??>
                            <span class="l-time">${conversation.conversation.createdDate?string("yyyy-MM-dd HH:mm:ss")}</span>
                        <#else >
                            <span class="l-time">2017-11-24 21:02</span>

                        </#if>

                        <div class="l-operate-bar">
                        <!--
                    <a href="javascript:void(0);" class="sns-action-del" data-id="10001_622873">
                    删除
                    </a>
                    -->
                        <a href="/msg/detail?conversationId=${conversation.conversation.conversationId}">
                            ${conversation.conversation.id}
                        </a>
                    </div>
                </div>
                <div class="chat-headbox">
                <span class="msg-num">
                $conversation.unread
                </span>
                    <a class="list-head">
                        <img alt="头像" src="${conversation.user.headUrl}">
                    </a>
                </div>
                <div class="letter-detail">
                    <a title="${conversation.user.name}" class="letter-name level-color-1">
                        ${conversation.user.name}
                    </a>
                    <p class="letter-brief">
                        <a href="/msg/detail?conversationId=${conversation.conversation.conversationId}">
                            ${conversation.conversation.content}
                        </a>
                    </p>
                </div>
            </li>
        </#list>
        </ul>

    </div>
</div>
<#include "header.ftl">
