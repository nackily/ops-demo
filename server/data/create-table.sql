CREATE TABLE IF NOT EXISTS server (
                        id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                        name TEXT NOT NULL,
                        ip TEXT NOT NULL,
                        online_status TEXT NOT NULL,
                        create_time DATETIME NOT NULL,
                        latest_report_time DATETIME,
                        report_interval_seconds INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS metrics_log (
                        id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                        server_id INTEGER NOT NULL,
                        os_name TEXT,
                        os_arch TEXT,
                        os TEXT,
                        cpu_wait_rate REAL,
                        cpu_idle_rate REAL,
                        cpu_os_usage_rate REAL,
                        cpu_user_usage_rate REAL,
                        cpu_usage_rate REAL,
                        cpu_processor_count INTEGER,
                        memory_total REAL,
                        memory_used REAL,
                        memory_usage_rate REAL,
                        disk_total REAL,
                        disk_used REAL,
                        disk_usage_rate REAL,
                        report_time DATETIME,
                        net_if TEXT,
                        disk_usage TEXT
);

CREATE TABLE IF NOT EXISTS order_log (
                             id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                             server_id INTEGER NOT NULL,
                             order_content TEXT,
                             issue_time DATETIME,
                             issue_succeeded INTEGER,
                             issue_error TEXT,
                             execute_time DATETIME,
                             execute_succeeded INTEGER,
                             execute_error TEXT,
                             execute_out TEXT,
                             report_time DATETIME
);