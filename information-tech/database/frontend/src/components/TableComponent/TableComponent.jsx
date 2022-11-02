import React, { useState, useEffect } from 'react';
import { toast } from 'react-toastify';
import { DB_SERVICE } from '../../config';
import { Icon, Dropdown } from 'semantic-ui-react';
import styles from './TableComponent.module.css';
import AddEntityComponent from '../AddEntityComponent/AddEntityComponent';

const TableComponent = ({ id, rows, columns, setColumns, setRows }) => {

	const [isColumnDialogVisible, setColumnDialogVisible] = useState(false);
	const [isRowDialogVisible, setRowDialogVisible] = useState(false);
	const [isRowEditDialogVisible, setRowEditDialogVisible] = useState(false);
	const [columnName, setColumnName] = useState('');
	const [columnType, setColumnType] = useState('');
	const [fields, setFields] = useState('');
	const [rid, setRid] = useState('');

	const fetchRows = () => {
		fetch(`${DB_SERVICE}/tables/${id}/rows`)
			.then(res => {
				if (res.status != 200) {
					return res.json().then(res => { throw new Error(res.message) });;
				}
				return res.json();
			}).then(res => {
				setRows(res);
				console.log('Got rows: ' + JSON.stringify(rows))
			})
			.catch((error) => {
				toast.error('Error! ' + error.message);
				console.log(`Error fetching table: ${error}`)
			});
	}

	const fetchColumns = () => {
		fetch(`${DB_SERVICE}/tables/${id}/columns`)
			.then(res => {
				if (res.status != 200) {
					return res.json().then(res => { throw new Error(res.message) });;
				}
				return res.json();
			}).then(res => setColumns(res))
			.catch((error) => {
				toast.error('Error! ' + error.message);
				console.log(`Error fetching columns: ${error}`)
			});
	}

	const addColumn = () => {
		fetch(`${DB_SERVICE}/tables/${id}/columns`, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify({
				name: columnName,
				type: columnType
			})
		}).then(res => {
			if (res.status != 201) {
				return res.json().then(res => { throw new Error(res.message) });;
			}
			return res.json()
		})
			.then(res => {
				setColumns([...columns, res]);
				fetchRows();
			})
			.catch((error) => {
				toast.error('Error! ' + error.message);
				console.log(`Error adding column: ${error}`)
			});
	}

	const deleteColumn = (cid) => {
		fetch(`${DB_SERVICE}/tables/${id}/columns/${cid}`, {
			method: 'DELETE',
			headers: {
				'Content-Type': 'application/json',
			}
		}).then(res => {
			if (res.status != 200) {
				return res.json().then(res => { throw new Error(res.message) });;
			}
		})
			.then(() => {
				setColumns([...columns].filter(c => c.id !== cid));
				fetchRows();
			})
			.catch((error) => {
				toast.error('Error! ' + error.message);
				console.log(`Error deleting column: ${error}`)
			});
	}

	const addRow = (fields) => {
		fields = fields.split(',');
		fetch(`${DB_SERVICE}/tables/${id}/rows`, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify({
				fields: fields,
			})
		}).then(res => {
			if (res.status != 201) {
				return res.json().then(res => { throw new Error(res.message) });;
			}
			return res.json()
		})
			.then(res => {
				setRows([...rows, res]);
			})
			.catch((error) => {
				toast.error('Error! ' + error.message);
				console.log(`Error adding row: ${error}`)
			});
	}

	const editRow = (fields) => {
		fields = fields.split(',');
		fetch(`${DB_SERVICE}/tables/${id}/rows/${rid}`, {
			method: 'PUT',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify({
				fields: fields,
			})
		}).then(res => {
			if (res.status != 200) {
				return res.json().then(res => { throw new Error(res.message) });;
			}
			return res.json()
		})
			.then(() => {
				fetchRows();
			})
			.catch((error) => {
				toast.error('Error! ' + error.message);
				console.log(`Error adding row: ${error}`)
			});
		setRid('');
	}

	const deleteRow = (rid) => {
		fetch(`${DB_SERVICE}/tables/${id}/rows/${rid}`, {
			method: 'DELETE',
			headers: {
				'Content-Type': 'application/json',
			}
		}).then(res => {
			if (res.status != 200) {
				return res.json().then(res => { throw new Error(res.message) });;
			}
		})
			.then(() => {
				setRows([...rows].filter(r => r.id !== rid));
			})
			.catch((error) => {
				toast.error('Error! ' + error.message);
				console.log(`Error deleting a row: ${error}`)
			});
	}


	const renderRow = (fields) => {
		return fields.map(f => (<div className={styles.row_item}>{f}</div>));
	};

	useEffect(() => {
		fetchColumns();
		fetchRows();
	}, []);

	return (
		<div className={styles.table_container}>
			<AddColumnDialog
				name={columnName}
				setName={setColumnName}
				type={columnType}
				setType={setColumnType}
				visible={isColumnDialogVisible}
				setVisible={setColumnDialogVisible}
				submit={(name, type) => addColumn(name, type)}
			/>
			<AddEntityComponent
				name={fields}
				setName={setFields}
				visible={isRowDialogVisible}
				setVisible={setRowDialogVisible}
				submit={() => addRow(fields)}
				text="Enter field values"
			/>
			<AddEntityComponent
				name={fields}
				setName={setFields}
				visible={isRowEditDialogVisible}
				setVisible={setRowEditDialogVisible}
				submit={() => editRow(fields)}
				text="Enter field values"
			/>
			<div className={styles.columns_container}>
				<button
					className={styles.add_btn}
					onClick={() => setColumnDialogVisible(true)}>
					Add Column
				</button>
				{columns.map(c => (
					<div className={styles.column_item}>
						<div className={styles.name_column_item}>
							{c.name}
							{columns.length > 0 && <button
								className={styles.item_btn}
								onClick={() => deleteColumn(c.id)}
							>
								<Icon name="delete" />
							</button>}
						</div>
						<div className={styles.type_column_item}>{c.type}</div>
					</div>
				))}
			</div>
			<div className={styles.rows_section}>
				<button className={styles.add_btn}
					onClick={() => setRowDialogVisible(true)}>
					Add Row
				</button>
				<div className={styles.rows_container}>
					{rows.map(r => (
						<div className={styles.row_list}>
							<div>{renderRow(r.fields)}</div>
							<button
								className={styles.small_btn}
								onClick={() => {
									setRid(r.id);
									setFields(r.fields);
									setRowEditDialogVisible(true);
								}}>
								<Icon name='edit' />
							</button>
							<button
								className={styles.small_btn}
								onClick={() => deleteRow(r.id)}>
								<Icon name='delete' />
							</button>
						</div>
					))}
				</div>
			</div>
		</div>
	);
}

const AddColumnDialog = ({ name, setName, type, setType, visible, setVisible, submit }) => {
	const onAddClick = () => {
		let inputName = name.trim();
		if (inputName === '' || type == '') {
			return;
		}
		submit(inputName, type);
		setVisible(false);
		setName('');
		setType('');
	}

	const typeOptions = [
		{ key: 'Integer', text: 'Integer', value: 'INTEGER' },
		{ key: 'Real', text: 'Real', value: 'REAL' },
		{ key: 'Char', text: 'Char', value: 'CHAR' },
		{ key: 'String', text: 'String', value: 'STRING' },
		{ key: 'Time', text: 'Time', value: 'TIME' },
		{ key: 'TimeInvl', text: 'Time Interval', value: 'TIMEINVL' },
	]

	return visible ? (
		<div className={styles.column_dialog}>
			<div className={styles.close_container}>
				<button
					className={styles.close_button}
					onClick={() => setVisible(false)}
				>
					<Icon name="close" />
				</button>
			</div>
			<div className={styles.add_header}>Enter the Column Name</div>
			<div>
				<input className={styles.add_input}
					type="text"
					value={name}
					onChange={(e) => setName(e.target.value)}
				/>
			</div>
			<div className={styles.type_header}>Column Type</div>
			<div>
				<Dropdown
					className={styles.dropdown}
					options={typeOptions}
					selection
					onChange={(_, data) => {
						setType(data.value)
						console.log(data.value);
					}}
				/>
			</div>
			<div className={styles.submit_container}>
				<button
					className={styles.submit_button}
					onClick={() => onAddClick()}>
					Submit
				</button>
			</div>
		</div>
	) : null;
}

export default TableComponent;