import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { ToastContainer, toast } from 'react-toastify';
import TableComponent from '../../components/TableComponent/TableComponent';
import { DB_SERVICE } from '../../config';
import styles from './TableView.module.css';

const TableView = () => {

	const { dbId, id } = useParams();

		const [table, setTable] = useState({ name: '' });
		const [columns, setColumns] = useState([]);
		const [rows, setRows] = useState([]);

	const getTable = () => {
		fetch(`${DB_SERVICE}/tables/${id}`)
			.then(res => {
				if (res.status != 200) {
					return res.json().then(res => { throw new Error(res.message) });;
				}
				return res.json();
			}).then(res => setTable(res))
			.catch((error) => {
				toast.error('Error! ' + error.message);
				console.log(`Error fetching table: ${error}`)
			});
	}

	useEffect(() => {
		getTable();
	}, []);

	return (
		<div className={styles.table_container}>
			<div className={styles.header}>
				{table.name}
			</div>
			<TableComponent
				rows={rows}
				columns={columns}
				setColumns={setColumns}
				id={id}
				setRows={setRows}/>
		</div>
	);

}

export default TableView;