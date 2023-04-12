
import { PlusOutlined } from '@ant-design/icons';
import type { ActionType, ProColumns, ProDescriptionsItemProps } from '@ant-design/pro-components';
import {
  FooterToolbar,
  ModalForm,
  PageContainer,
  ProDescriptions,
  ProFormText,
  ProFormTextArea,
  ProTable,
} from '@ant-design/pro-components';
import { FormattedMessage, useIntl } from '@umijs/max';
import {Button, Drawer,  message} from 'antd';
import React, { useRef, useState } from 'react';
import {
  addInterfaceInfoUsingPOST,
  deleteInterfaceInfoUsingPOST,
  listInterfaceInfoByPageUsingGET,
  offlineInterfaceInfoUsingPOST,
  onlineInterfaceInfoUsingPOST,
  updateInterfaceInfoUsingPOST
} from "@/services/api-backend/interfaceInfoController";
import  type {SortOrder} from "antd/es/table/interface";
import CreateModal from "@/pages/Admin/InterfaceInfo/components/CreateForm";
import UpdateModal from "./components/UpdateForm";







const TableList: React.FC = () => {
  /**
   * @en-US Pop-up window of new window
   * @zh-CN 新建窗口的弹窗
   *  */
  const [createModalOpen, handleModalOpen] = useState<boolean>(false);
  /**
   * @en-US The pop-up window of the distribution update window
   * @zh-CN 分布更新窗口的弹窗
   * */
  const [updateModalOpen, handleUpdateModalOpen] = useState<boolean>(false);

  const [showDetail, setShowDetail] = useState<boolean>(false);

  const actionRef = useRef<ActionType>();
  const [currentRow, setCurrentRow] = useState<API.InterfaceInfo>();
  const [selectedRowsState, setSelectedRows] = useState<API.InterfaceInfo[]>([]);

  /**
   * @en-US Add node
   * @zh-CN 添加节点
   * @param fields
   */
  const handleAddInterfaceInfo = async (fields: API.InterfaceInfo) => {
    const hide = message.loading('正在添加');
    try {
      await addInterfaceInfoUsingPOST({ ...fields });
      hide();
      message.success('创建成功');
      //创建成功，关闭模态框
      handleModalOpen(false);
      //刷新页面
      actionRef.current?.reload();
      return true;
    } catch (error :any) {
      hide();
      console.log(error)
      message.error('创建失败, ' + error.message);
      return false;
    }
  };

  /**
   * @en-US Update node
   * @zh-CN 更新节点
   *
   * @param fields
   */
  const handleUpdate = async (fields: API.InterfaceInfoUpdateRequest) => {
    const hide = message.loading('正在更新');
    try {
      if(!currentRow){
        return false;
      }
      let res = await updateInterfaceInfoUsingPOST({
        // 因为columns中的id valueType为index 不会传递 所以我们需要手动赋值id
        id: currentRow.id,
        ...fields,
      });
      if (res.data) {
        hide();
        handleUpdateModalOpen(false);
        message.success('更新成功!');
        // 刷新页面
        actionRef.current?.reload();
        return true;
      }
    } catch (error: any) {
      hide();
      message.error('更新失败!' + error.message);
      return false;
    }
  };

  /**
   *  Delete node
   * @zh-CN 删除节点
   *
   * @param record
   */
  const handleRemove = async (record: API.InterfaceInfo) => {
    const hide = message.loading('正在删除');
    if (!record) return true;
    try {
      await deleteInterfaceInfoUsingPOST({
         id: record.id
      });
      hide();
      message.success('删除成功');
      actionRef.current?.reload();
      return true;
    } catch (error :any) {
      hide();
      message.error('删除失败' + error.message);
      return false;
    }
  };
  /**
   * @en-US Add node
   * @zh-CN 发布接口
   * @param record
   */
  const handleOnline = async (record: API.IdRequest) => {
    const hide = message.loading('发布中');
    if (!record) return true;
    try {
      await onlineInterfaceInfoUsingPOST({
        id: record.id
      });
      hide();
      message.success('操作成功');
      actionRef.current?.reload();
      return true;
    } catch (error :any) {
      hide();
      message.error('操作失败' + error.message);
      return false;
    }
  };

  /**

   * @zh-CN 下线接口
   * @param record
   */
  const handleOffline = async (record: API.IdRequest) => {
    const hide = message.loading('下线中');
    if (!record) return true;
    try {
      await offlineInterfaceInfoUsingPOST({
        id: record.id
      });
      hide();
      message.success('操作成功');
      actionRef.current?.reload();
      return true;
    } catch (error :any) {
      hide();
      message.error('操作失败' + error.message);
      return false;
    }
  };


  /**
   * @en-US International configuration
   * @zh-CN 国际化配置
   * */
  const intl = useIntl();

  const columns: ProColumns<API.InterfaceInfo>[] = [{
    title: 'id',
    dataIndex: 'id',
    valueType: 'index',
  },
  {
    title: '接口名称',
    dataIndex: 'name',
    valueType: 'text',
    formItemProps: {
      rules:[{
        required:true,
        message:'必填项'
      }],
      required: true
    }
  },
  {
    title: '描述',
    dataIndex: 'description',
    valueType: 'textarea',
  },
  {
    title: '请求方法',
    dataIndex: 'method',
    valueType: 'text',
  },
  {
    title: 'url',
    dataIndex: 'url',
    valueType: 'text',
  },
    {
      title: '请求头',
      dataIndex: 'request_header',
      valueType: 'jsonCode',
    },
    {
      title: '响应头',
      dataIndex: 'response_header',
      valueType: 'jsonCode',
    },
    {
      title: '请求参数',
      dataIndex: 'requestParams',
      valueType: 'jsonCode',
    },
  {
    title: '状态',
    dataIndex: 'status',
    hideInForm: true,
    valueEnum: {
      0: {
        text: '关闭',
        status: 'Default',
      },
      1: {
        text: '开启',
        status: 'Processing',
      },
    },
  },
  {
    title: '创建时间',
    dataIndex: 'create_time',
    valueType: 'dateTime',
    hideInForm: true,
  },
  {
    title: '更新时间',
    dataIndex: 'update_time',
    valueType: 'dateTime',
    hideInForm: true,
  },
    {
      title: '操作',
      dataIndex: 'option',
      valueType: 'option',
      render: (_, record) => [
        <a
          key="config"
          type={"link"}
          onClick={() => {
            handleUpdateModalOpen(true);
            // @ts-ignore
            setCurrentRow(record);
          }}
        >
          修改
        </a>,
        //根据status判断，如果是下线状态，显示一个发布按钮
        record.status ===0 ?
        <a
          key="online"
          type={'link'}
          onClick={() => {
            handleOnline(record);
          }}
        >
          发布
        </a> : null,
        //根据status判断，如果是上线状态，显示一个下线按钮
        record.status ===1 ? <Button
          type={"text"}
          danger
          key="text"
          onClick={() => {
            handleOffline(record);
          }}
        >
          下线
        </Button> :null,
         <Button
          type={"text"}
          danger
          key="delete"
          onClick={() => {
            handleRemove(record);
          }}
        >
          删除
         </Button>,
      ],
    },
  ];

  return (
    <PageContainer>
      <ProTable<API.RuleListItem, API.PageParams>
        headerTitle={intl.formatMessage({
          id: 'pages.searchTable.title',
          defaultMessage: 'Enquiry form',
        })}
        actionRef={actionRef}
        rowKey="key"
        search={{
          labelWidth: 120,
        }}
        toolBarRender={() => [
          <Button
            type="primary"
            key="primary"
            onClick={() => {
              handleModalOpen(true);
            }}
          >
            <PlusOutlined /> <FormattedMessage id="pages.searchTable.new" defaultMessage="新建" />
          </Button>,
        ]}
        request={async (
          params: {
            pageSize?: number;
            current?: number;
            keyword?: string;
          }, sort: Record<string, SortOrder>,  filter: Record<string, React.ReactText[] | null>,) => {
          const res :any = await listInterfaceInfoByPageUsingGET({
            ...params });
          if (res?.data) {
            return {
              data: res.data.records || [],
              success: true,
              total: res.data.total || 0,
            };
          } else {
            return {
              data: [],
              success: false,
              total: 0,
            };
          }
        }}
        columns={columns}
        rowSelection={{
          onChange: (_, selectedRows) => {
            setSelectedRows(selectedRows);
          },
        }}
      />
      {selectedRowsState?.length > 0 && (
        <FooterToolbar
          extra={
            <div>
              <FormattedMessage id="pages.searchTable.chosen" defaultMessage="Chosen" />{' '}
              <a style={{ fontWeight: 600 }}>{selectedRowsState.length}</a>{' '}
              <FormattedMessage id="pages.searchTable.item" defaultMessage="项" />
              &nbsp;&nbsp;
              <span>
                <FormattedMessage
                  id="pages.searchTable.totalServiceCalls"
                  defaultMessage="Total number of service calls"
                />{' '}
                {selectedRowsState.reduce((pre, item) => pre + item.callNo!, 0)}{' '}
                <FormattedMessage id="pages.searchTable.tenThousand" defaultMessage="万" />
              </span>
            </div>
          }
        >
          <Button
            onClick={async () => {
              await handleRemove(selectedRowsState);
              setSelectedRows([]);
              actionRef.current?.reloadAndRest?.();
            }}
          >
            <FormattedMessage
              id="pages.searchTable.batchDeletion"
              defaultMessage="Batch deletion"
            />
          </Button>
          <Button type="primary">
            <FormattedMessage
              id="pages.searchTable.batchApproval"
              defaultMessage="Batch approval"
            />
          </Button>
        </FooterToolbar>
      )}
      <ModalForm
        title={intl.formatMessage({
          id: 'pages.searchTable.createForm.newRule',
          defaultMessage: 'New rule',
        })}
        width="400px"
        open={createModalOpen}
        onOpenChange={handleModalOpen}
        onFinish={async (value) => {
          const success = await handleAddInterfaceInfo(value as API.RuleListItem);
          if (success) {
            handleModalOpen(false);
            if (actionRef.current) {
              actionRef.current.reload();
            }
          }
        }}
      >
        <ProFormText
          rules={[
            {
              required: true,
              message: (
                <FormattedMessage
                  id="pages.searchTable.ruleName"
                  defaultMessage="Rule name is required"
                />
              ),
            },
          ]}
          width="md"
          name="name"
        />
        <ProFormTextArea width="md" name="desc" />
      </ModalForm>
      <UpdateModal
        columns={columns}
        onSubmit={async (value) => {
          const success = await handleUpdate(value);
          if (success) {
            handleUpdateModalOpen(false);
            setCurrentRow(undefined);
            if (actionRef.current) {
              actionRef.current.reload();
            }
          }
        }}
        onCancel={() => {
          handleUpdateModalOpen(false);
          if (!showDetail) {
            setCurrentRow(undefined);
          }
        }}
        open={updateModalOpen}
        values={currentRow || {}}
      />

      <Drawer
        width={600}
        open={showDetail}
        onClose={() => {
          setCurrentRow(undefined);
          setShowDetail(false);
        }}
        closable={false}
      >
        {currentRow?.name && (
          <ProDescriptions<API.RuleListItem>
            column={2}
            title={currentRow?.name}
            request={async () => ({
              data: currentRow || {},
            })}
            params={{
              id: currentRow?.name,
            }}
            columns={columns as ProDescriptionsItemProps<API.RuleListItem>[]}
          />
        )}
      </Drawer>
      <CreateModal
        columns={columns}
        onCancel={() => handleModalOpen(false)}
        onSubmit={(values) => {handleAddInterfaceInfo(values)}}
        open={createModalOpen}
      />
    </PageContainer>
  );
};

export default TableList;
