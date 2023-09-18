<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>已发送群消息列表</title>
    <link rel="stylesheet" type="text/css" href="${contextPath}/bootstrap/5.1.3/css/bootstrap.css"/>
    <script type="text/javascript" src="${contextPath}/bootstrap/5.1.3/js/bootstrap.bundle.js"></script>
    <style>
        .table tbody tr td{
            overflow: hidden;
            text-overflow:ellipsis;
            white-space: nowrap;
        }
    </style>
</head>
<body>
<div class="container-fluid">
    <form class="row g-3 mb-3 mt-3" method="POST">
        <div class="col-6">
            <label class="form-check-label" for="type">平台</label>
            <select class="form-select" id="type" name="type" aria-label="选择平台">
                <option value="" <#if ((query.type)!'') == ''>selected</#if>>All</option>
                <option value="DINGTALK" <#if ((query.type)!'') == 'DINGTALK'>selected</#if>>钉钉
                </option>
                <option value="WEIXIN" <#if ((query.type)!'') == 'WEIXIN'>selected</#if>>企业微信
                </option>
                <option value="FEISHU" <#if ((query.type)!'') == 'FEISHU'>selected</#if>>飞书</option>
            </select>
        </div>
        <div class="col-6">
            <label class="form-check-label" for="alias">别名</label>
            <input type="text" class="form-control" id="alias" name="alias" aria-describedby="平台别名"
                   value="${(query.alias)!''}">
        </div>
        <div class="col-6">
            <label class="form-check-label" for="request">请求内容</label>
            <input type="text" class="form-control" id="request" name="request"
                   aria-describedby="请求内容"
                   value="${(query.request)!''}">
        </div>
        <div class="col-6">
            <label class="form-check-label" for="response">响应内容</label>
            <input type="text" class="form-control" id="response" name="response"
                   aria-describedby="响应内容"
                   value="${(query.response)!''}">
        </div>
        <div class="col-12">
            <button type="submit" class="btn btn-primary">查询</button>
        </div>
    </form>
    <div class="row">
        <div class="col-12" style="overflow-x: auto">
            <table class="table table-striped table-border">
                <thead>
                <tr>
                    <th scope="col">#</th>
                    <th scope="col">平台</th>
                    <th scope="col">别名</th>
                    <th scope="col">请求</th>
                    <th scope="col">响应</th>
                </tr>
                </thead>
                <tbody>
                <#if list?? && (list?size > 0)>
                    <#list list as row>
                        <tr>
                            <#--<th scope="row">${row.id}</th>-->
                            <th scope="row">${row_index + 1}</th>
                            <td>${row.type!'-'}</td>
                            <td>${row.alias!'-'}</td>
                            <td>${row.request!'-'}</td>
                            <td>${row.response!'-'}</td>
                        </tr>
                    </#list>
                </#if>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>