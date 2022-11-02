import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import styles from './IntersectView.module.css';
import { toast } from 'react-toastify';
import { DB_SERVICE } from '../../config';
import { Dropdown } from 'semantic-ui-react';

const IntersectView = () => {

	const { id } = useParams();

	const [tables, setTables] = useState([]);
	const [table1, setTable1] = useState('');
	const [table2, setTable2] = useState('');

	const [columns, setColumns] = useState([]);
	const [rows, setRows] = useState([]);

	const getTables = (id) => {
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

	const intersectTables = () => {
		fetch(`${DB_SERVICE}/tables/intersect?leftId=${table1}&rightId=${table2}`)
			.then(res => {
				if (res.status != 200) {
					return res.json().then(res => { throw new Error(res.message) });;
				}
				return res.json()
			})
			.then(res => { setRows(res.rows); setColumns(res.columns) })
			.catch((error) => {
				toast.error('Error! ' + error.message);
				console.log(`Error intersecting tables: ${error}`)
			});
	}

	useEffect(() => {
		getTables(id);
	}, [id]);

	const getOptions = () => {
		return tables.map(t => ({ key: t.id, text: t.name, value: t.id }));
	}

	const renderRow = (fields) => {
		return fields.map(f => (<div className={styles.row_item}>{f}</div>));
	};

	return (
		<div className={styles.intersect_container}>
			<div className={styles.header}>Tables Intersection</div>
			<div className={styles.choice_container}>
				<Dropdown
					className={styles.dropdown}
					options={getOptions()}
					selection
					onChange={(_, data) => {
						setTable1(data.value)
					}}
				/>
				<Dropdown
					className={styles.dropdown}
					options={getOptions()}
					selection
					onChange={(_, data) => {
						setTable2(data.value)
					}}
				/>
				<button
					className={styles.add_btn}
					onClick={() => intersectTables()}>
					Find Intersection
				</button>
			</div>
			<div className={styles.columns_container}>
				{columns.map(c => (
					<div className={styles.column_item}>
						<div className={styles.name_column_item}>{c.name}</div>
						<div className={styles.type_column_item}>{c.type}</div>
					</div>
				))}
			</div>
			<div className={styles.rows_section}>
				<div className={styles.rows_container}>
					{rows.map(r => (
						<div className={styles.row_list}>{renderRow(r.fields)}</div>
					))}
				</div>
			</div>
		</div>
	);
}

export default IntersectView;