import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { ToastContainer, toast } from 'react-toastify';
import AddEntityComponent from '../../components/AddEntityComponent/AddEntityComponent';
import { DB_SERVICE } from '../../config';
import styles from './DatabaseView.module.css';

const DatabaseView = () => {

	const [database, setDatabase] = useState({ name: '' });
	const [tables, setTables] = useState([]);
	const [tableName, setTableName] = useState('');
	const [popupVisible, setPopupVisible] = useState(false);
	const [editPopupVisible, setEditPopupVisible] = useState(false);
	const { id } = useParams();
	const navigate = useNavigate();

	const onItemClick = (tableId) => {
		navigate(`/${id}/table/${tableId}`);
	}

	const modifyDatabase = () => {
		fetch(`${DB_SERVICE}/databases/${id}`, {
			method: 'PUT',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify({
				name: tableName
			})
		}).then(res => {
			if (res.status != 200) {
				return res.json().then(res => { throw new Error(res.message) });;
			}
			return res.json()
		})
			.then(res => setDatabase(res))
			.catch((error) => {
				toast.error('Error! ' + error.message);
				console.log(`Error fetching database: ${error}`)
			});
	}

	const deleteDatabase = (id) => {
		fetch(`${DB_SERVICE}/databases/${id}`, {
			method: 'DELETE'
		})
			.then(res => {
				if (res.status != 200) {
					return res.json().then(res => { throw new Error(res.message) });;
				}
				navigate('/');
			})
			.catch((error) => {
				toast.error('Error! ' + error.message);
				console.log(`Error deleting database: ${error}`)
			});
	}

	const getDatabase = (id) => {
		fetch(`${DB_SERVICE}/databases/${id}`)
			.then(res => {
				if (res.status != 200) {
					return res.json().then(res => { throw new Error(res.message) });;
				}
				return res.json()
			})
			.then(res => setDatabase(res))
			.catch((error) => {
				toast.error('Error! ' + error.message);
				console.log(`Error fetching database: ${error}`)
			});
	}

	const getTables = () => {
		fetch(`${DB_SERVICE}/databases/${id}/tables`)
			.then(res => {
				if (res.status != 200) {
					return res.json().then(res => { throw new Error(res.message) });;
				}
				return res.json()
			})
			.then(res => setTables(res))
			.catch((error) => {
				toast.error('Error! ' + error.message);
				console.log(`Error fetching tables: ${error}`)
			});
	}

	const addTable = () => {
		fetch(`${DB_SERVICE}/databases/${id}/tables`, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify({
				name: tableName
			})
		})
			.then(res => {
				if (res.status != 201) {
					return res.json().then(res => { throw new Error(res.message) });;
				}
				return res.json()
			})
			.then(res => setTables([...tables, res]))
			.catch((error) => {
				toast.error('Error! ' + error.message);
				console.log(`Error adding a table: ${error}`)
			});
	}

	useEffect(() => {
		getDatabase(id);
		getTables();
	}, []);

	return (
		<div className={styles.database_container}>
			<ToastContainer
				autoClose={2000}
				theme="dark" />
			<div className={styles.header}>{database.name}</div>
			<AddEntityComponent
				name={tableName}
				setName={setTableName}
				submit={() => addTable()}
				visible={popupVisible}
				setVisible={setPopupVisible}
			/>
			<AddEntityComponent
				name={tableName}
				setName={setTableName}
				submit={() => modifyDatabase()}
				visible={editPopupVisible}
				setVisible={setEditPopupVisible}
			/>
			<div className={styles.controls_container}>
				<button
					className={styles.add_button}
					onClick={() => setPopupVisible(true)}
				>
					Add Table
				</button>
				<button
					className={styles.add_button}
					onClick={() => deleteDatabase(id)}
				>
					Delete Database
				</button>
				<button
					className={styles.add_button}
					onClick={() => setEditPopupVisible(true)}
				>
					Edit Database
				</button>
				<button
					className={styles.add_button}
					onClick={() => navigate(`/${id}/intersect`)}
				>
					Tables Intersection
				</button>
				<button
					className={styles.back_button}
					onClick={() => navigate('/')}
				>
					Back
				</button>
			</div>
			<div className={styles.tables_container}>
				{tables.map(t => <TableItem
					name={t.name}
					onItemClick={() => onItemClick(t.id)}
				/>)}
			</div>
		</div>

	);
}

const TableItem = ({ name, onItemClick }) => {
	return (
		<div className={styles.item_container}>
			<div className={styles.item_name}
				onClick={() => onItemClick()}
			>
				{name}
			</div>
		</div>
	);
}

export default DatabaseView;