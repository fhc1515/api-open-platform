import { Modal } from 'antd';
import React, {useEffect, useRef} from 'react';
import { ProColumns, ProFormInstance, ProTable } from '@ant-design/pro-components';
import '@umijs/max';

export type Props = {
  values: API.InterfaceInfo;
  columns: ProColumns<API.InterfaceInfoUpdateRequest>[];
  onCancel: () => void;
  onSubmit: (values: API.InterfaceInfoUpdateRequest) => Promise<void>;
  open: boolean;
};

const UpdateModal: React.FC<Props> = (props) => {
  const { values, columns, open, onCancel, onSubmit } = props;

  const formRef = useRef<ProFormInstance>();

  useEffect(() => {
    if (formRef) {
      formRef.current?.setFieldsValue(values);
    }
  }, [values]);

  return (
    <Modal title={'更新接口'} footer={null} open={open} onCancel={() => onCancel?.()}>
      <ProTable
        columns={columns}
        formRef={formRef}
        type={'form'}
        onSubmit={async (value) => onSubmit?.(value)}
        // 设置默认值
        form={{ initialValues: values }}
      />
    </Modal>
  );
};

export default UpdateModal;
