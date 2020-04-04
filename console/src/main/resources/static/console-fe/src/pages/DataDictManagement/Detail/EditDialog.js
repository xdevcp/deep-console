import React from 'react';
import PropTypes from 'prop-types';
import { request } from '../../../globalLib';
import { Dialog, Form, Input, Select, Message, ConfigProvider } from '@alifd/next';
import { DIALOG_FORM_LAYOUT, METADATA_SEPARATOR, METADATA_ENTER } from './constant';
import MonacoEditor from 'components/MonacoEditor';

@ConfigProvider.config
class DataDictEditDialog extends React.Component {
  static displayName = 'DataDictEditDialog';

  static propTypes = {
    queryDataDictList: PropTypes.func,
    getDataDictDetail: PropTypes.func,
    locale: PropTypes.object,
  };

  constructor(props) {
    super(props);
    this.state = {
      isCreate: false,
      editData: {},
      EditDialogVisible: false,
      errors: { name: {}, protectThreshold: {} },
    };
    this.show = this.show.bind(this);
  }

  show(_editData = {}) {
    let editData = _editData;
    const { metadata = {}, name } = editData;
    if (Object.keys(metadata).length) {
      editData.metadataText = JSON.stringify(metadata, null, '\t');
    }
    this.setState({ editData, EditDialogVisible: true, isCreate: !name });
  }

  hide() {
    this.setState({ EditDialogVisible: false });
  }

  validator(field) {
    const { locale = {} } = this.props;
    const errors = Object.assign({}, this.state.errors);
    const helpMap = {
      name: locale.fieldRequired,
      protectThreshold: locale.protectThresholdRequired,
    };
    if (field.protectThreshold === 0) {
      field.protectThreshold = '0';
    }
    for (const key in field) {
      if (!field[key]) {
        errors[key] = { validateState: 'error', help: helpMap[key] };
        this.setState({ errors });
        return false;
      }
    }
    return true;
  }

  onConfirm() {
    const { isCreate } = this.state;
    const editData = Object.assign({}, this.state.editData);
    const { name, protectThreshold, groupName, metadataText = '', selector } = editData;
    if (!this.validator({ name, protectThreshold })) return;
    request({
      method: isCreate ? 'POST' : 'PUT',
      url: 'v1/open/dict',
      data: {
        dataType: name,
        dataCode: groupName || 'DEFAULT_GROUP',
        dataValue: metadataText,
        dataDesc: name,
        sortNo: JSON.stringify(selector),
        status: 0,
      },
      dataType: 'text',
      beforeSend: () => this.setState({ loading: true }),
      success: res => {
        if (res !== 'ok') {
          Message.error(res);
          return;
        }
        if (isCreate) {
          this.props.queryDataDictList();
        } else {
          this.props.getDataDictDetail();
        }
      },
      error: res => Message.error(res.responseText || res.statusText),
      complete: () => this.setState({ loading: false }),
    });
    this.hide();
  }

  onChangeCluster(changeVal) {
    const resetKey = ['name', 'protectThreshold'];
    const { editData = {} } = this.state;
    const errors = Object.assign({}, this.state.errors);
    resetKey.forEach(key => {
      if (changeVal[key]) {
        errors[key] = {};
        this.setState({ errors });
      }
    });
    this.setState({
      editData: Object.assign({}, editData, changeVal),
    });
  }

  getFormItemLayout = () => ({
    labelCol: { span: 6 },
    wrapperCol: { span: 14 },
  });

  render() {
    const { locale = {} } = this.props;
    const { isCreate, editData, EditDialogVisible, errors } = this.state;
    const {
      name,
      protectThreshold,
      groupName,
      metadataText,
      selector = { type: 'none' },
    } = editData;
    const formItemLayout = this.getFormItemLayout();
    return (
      <Dialog
        className="main-detail-edit-dialog"
        title={isCreate ? locale.create : locale.update}
        visible={EditDialogVisible}
        onOk={() => this.onConfirm()}
        onCancel={() => this.hide()}
        onClose={() => this.hide()}
      >
        <Form {...DIALOG_FORM_LAYOUT}>
          <Form.Item
            required={isCreate}
            {...formItemLayout}
            label={`${locale.field1}:`}
            {...errors.name}
          >
            {!isCreate ? (
              <p>{name}</p>
            ) : (
              <Input value={name} onChange={name => this.onChangeCluster({ name })} />
            )}
          </Form.Item>
          <Form.Item
            required
            {...formItemLayout}
            label={`${locale.protectThreshold}:`}
            {...errors.protectThreshold}
          >
            <Input
              value={protectThreshold}
              onChange={protectThreshold => this.onChangeCluster({ protectThreshold })}
            />
          </Form.Item>
          <Form.Item {...formItemLayout} label={`${locale.groupName}:`}>
            <Input
              defaultValue={groupName}
              placeholder="DEFAULT_GROUP"
              readOnly={!isCreate}
              onChange={groupName => this.onChangeCluster({ groupName })}
            />
          </Form.Item>
          <Form.Item label={`${locale.dataDesc}:`} {...formItemLayout}>
            <MonacoEditor
              language="json"
              width={'100%'}
              height={200}
              value={metadataText}
              onChange={metadataText => this.onChangeCluster({ metadataText })}
            />
          </Form.Item>
          <Form.Item label={`${locale.type}:`} {...formItemLayout}>
            <Select
              className="full-width"
              defaultValue={selector.type}
              onChange={type => this.onChangeCluster({ selector: { ...selector, type } })}
            >
              <Select.Option value="label">{locale.typeLabel}</Select.Option>
              <Select.Option value="none">{locale.typeNone}</Select.Option>
            </Select>
          </Form.Item>
          {selector.type === 'label' && (
            <Form.Item label={`${locale.selector}:`} {...formItemLayout}>
              <Input.TextArea
                value={selector.expression}
                onChange={expression =>
                  this.onChangeCluster({ selector: { ...selector, expression } })
                }
              />
            </Form.Item>
          )}
        </Form>
      </Dialog>
    );
  }
}

export default DataDictEditDialog;
