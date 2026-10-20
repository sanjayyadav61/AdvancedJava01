<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="x" uri="jakarta.tags.xml" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%-- ==================================================================== --%>
<%-- RTU Syllabus Demonstration: Traditional JSP Elements Syntax         --%>
<%-- ==================================================================== --%>
<%! 
    // 1. JSP Declaration: Declares class-level variables or methods
    private int hitCounter = 1042;
    public String getAcademicHeading() {
        return "RTU Advanced Java / Java Enterprise E-Commerce Project";
    }
%>

<%
    // 2. JSP Scriptlet: Executes Java code block inside _jspService()
    hitCounter++;
    String candidateName = "Final Year RTU Scholar";
    String todayDate = new java.text.SimpleDateFormat("dd MMMM yyyy").format(new java.util.Date());
%>

<c:set var="pageTitle" value="RTU Syllabus Demos | ShopSphere" scope="request"/>
<%@ include file="fragments/header.jsp" %>
<%@ include file="fragments/navbar.jsp" %>

<main class="py-4">
    <div class="container">
        
        <div class="d-flex flex-wrap justify-content-between align-items-center mb-4 pb-3 border-bottom">
            <div>
                <span class="badge bg-primary px-3 py-1 rounded-pill fw-semibold text-uppercase small">Academic Practical</span>
                <h3 class="fw-bold mb-0 mt-1">RTU Advanced Java Syllabus Live Showcase</h3>
                <small class="text-muted">Interactive verification of all 30+ syllabus curriculum topics</small>
            </div>
            
            <!-- Internationalization Language Switcher (RTU Syllabus Topic) -->
            <div class="d-flex align-items-center gap-2">
                <span class="small fw-semibold text-muted"><i class="bi bi-translate text-primary me-1"></i>Language (i18n):</span>
                <div class="btn-group btn-group-sm" role="group">
                    <a href="${pageContext.request.contextPath}/demos?lang=en" class="btn ${activeLang == 'en' ? 'btn-primary' : 'btn-outline-primary'}">English</a>
                    <a href="${pageContext.request.contextPath}/demos?lang=hi" class="btn ${activeLang == 'hi' ? 'btn-primary' : 'btn-outline-primary'}">हिंदी (Hindi)</a>
                </div>
            </div>
        </div>

        <!-- Navigation Tabs for Topics -->
        <ul class="nav nav-pills nav-fill mb-4 bg-white p-2 rounded-4 shadow-sm border" id="demoTabs" role="tablist">
            <li class="nav-item">
                <button class="nav-link active fw-semibold" id="jsp-tab" data-bs-toggle="tab" data-bs-target="#tab-jsp" type="button">
                    <i class="bi bi-code-slash me-1"></i> 1. JSP Elements & EL
                </button>
            </li>
            <li class="nav-item">
                <button class="nav-link fw-semibold" id="jstl-tab" data-bs-toggle="tab" data-bs-target="#tab-jstl" type="button">
                    <i class="bi bi-tags me-1"></i> 2. JSTL Core & Fn
                </button>
            </li>
            <li class="nav-item">
                <button class="nav-link fw-semibold" id="xml-tab" data-bs-toggle="tab" data-bs-target="#tab-xml" type="button">
                    <i class="bi bi-filetype-xml me-1"></i> 3. JSTL XML Feed
                </button>
            </li>
            <li class="nav-item">
                <button class="nav-link fw-semibold" id="i18n-tab" data-bs-toggle="tab" data-bs-target="#tab-i18n" type="button">
                    <i class="bi bi-globe me-1"></i> 4. i18n & Bundles
                </button>
            </li>
            <li class="nav-item">
                <button class="nav-link fw-semibold" id="net-tab" data-bs-toggle="tab" data-bs-target="#tab-net" type="button">
                    <i class="bi bi-hdd-network me-1"></i> 5. Sockets & RMI
                </button>
            </li>
            <li class="nav-item">
                <button class="nav-link fw-semibold" id="applet-tab" data-bs-toggle="tab" data-bs-target="#tab-applet" type="button">
                    <i class="bi bi-window-sidebar me-1"></i> 6. Applets & JNDI
                </button>
            </li>
        </ul>

        <div class="tab-content" id="demoTabContent">

            <!-- TAB 1: Traditional JSP Elements & Expression Language -->
            <div class="tab-pane fade show active" id="tab-jsp" role="tabpanel">
                <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
                    <h5 class="fw-bold mb-3 text-primary"><i class="bi bi-journal-code me-2"></i>Topic: JSP Architecture, Lifecycle & Elements</h5>
                    <p class="text-secondary small">
                        The RTU syllabus requires students to demonstrate the 4 traditional JSP syntax constructs alongside modern Expression Language (EL).
                    </p>

                    <div class="table-responsive">
                        <table class="table table-bordered align-middle">
                            <thead class="table-light small text-uppercase">
                                <tr>
                                    <th style="width: 25%;">JSP Element Type</th>
                                    <th style="width: 40%;">Source Code Syntax in this Page</th>
                                    <th style="width: 35%;">Live Evaluated Output</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td><strong>1. Declaration</strong> <code>&lt;%! ... %&gt;</code></td>
                                    <td><code>&lt;%! private int hitCounter = 1042; %&gt;</code></td>
                                    <td><span class="badge bg-success">Class Member Loaded</span></td>
                                </tr>
                                <tr>
                                    <td><strong>2. Scriptlet</strong> <code>&lt;% ... %&gt;</code></td>
                                    <td><code>&lt;% hitCounter++; String candidateName = "..."; %&gt;</code></td>
                                    <td><span class="badge bg-info text-dark">Executed in _jspService()</span></td>
                                </tr>
                                <tr>
                                    <td><strong>3. Expression</strong> <code>&lt;%= ... %&gt;</code></td>
                                    <td><code>&lt;%= getAcademicHeading() %&gt;</code></td>
                                    <td class="fw-bold text-primary"><%= getAcademicHeading() %></td>
                                </tr>
                                <tr>
                                    <td><strong>4. JSP Comment</strong> <code>&lt;%-- ... --%&gt;</code></td>
                                    <td><code>&lt;%-- RTU Syllabus Demonstration --%&gt;</code></td>
                                    <td><em class="text-muted small">Server-side comment (omitted from HTML output)</em></td>
                                </tr>
                                <tr>
                                    <td><strong>5. Expression Language (EL)</strong> <code>\${...}</code></td>
                                    <td><code>\${applicationScope.appName} | \${sessionScope.user.name}</code></td>
                                    <td class="fw-bold text-success">${applicationScope.appName} (User: ${empty sessionScope.user ? 'Guest Scholar' : sessionScope.user.name})</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>

                    <!-- Custom Tag Files Demonstration -->
                    <div class="mt-4 p-3 bg-light rounded-3 border">
                        <h6 class="fw-bold mb-2"><i class="bi bi-stars text-warning me-1"></i>Demonstration: JSP Custom Tag Extensions & Tag Files</h6>
                        <p class="text-muted small mb-2">Rendered using <code>&lt;tags:rating rating="4.5" /&gt;</code>:</p>
                        <tags:rating rating="4.5" />
                    </div>
                </div>
            </div>

            <!-- TAB 2: JSTL Core & Functions -->
            <div class="tab-pane fade" id="tab-jstl" role="tabpanel">
                <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
                    <h5 class="fw-bold mb-3 text-primary"><i class="bi bi-tags-fill me-2"></i>Topic: JSTL Core Tags & Functions</h5>
                    
                    <div class="row g-4">
                        <div class="col-md-6">
                            <div class="border rounded-3 p-3 h-100">
                                <h6 class="fw-bold mb-3">1. JSTL Core: &lt;c:forEach&gt; & &lt;c:choose&gt;</h6>
                                <ul class="list-group list-group-flush small">
                                    <c:forEach var="p" items="${demoProducts}" begin="0" end="3">
                                        <li class="list-group-item d-flex justify-content-between align-items-center px-0">
                                            <span>${p.name}</span>
                                            <c:choose>
                                                <c:when test="${p.discount > 0}">
                                                    <span class="badge bg-danger rounded-pill">${p.discount}% OFF</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-secondary rounded-pill">Standard</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </div>

                        <div class="col-md-6">
                            <div class="border rounded-3 p-3 h-100">
                                <h6 class="fw-bold mb-3">2. JSTL Functions (fn:*)</h6>
                                <c:set var="sampleText" value="ShopSphere E-Commerce Platform" />
                                <div class="small d-flex flex-column gap-2">
                                    <div><strong>Original String:</strong> <code>"${sampleText}"</code></div>
                                    <div><strong>fn:toUpperCase:</strong> <span class="text-primary fw-bold">${fn:toUpperCase(sampleText)}</span></div>
                                    <div><strong>fn:length:</strong> <span class="badge bg-info text-dark">${fn:length(sampleText)} characters</span></div>
                                    <div><strong>fn:contains('Commerce'):</strong> <span class="badge bg-success">${fn:contains(sampleText, 'Commerce')}</span></div>
                                    <div><strong>fn:substring(0, 10):</strong> <code>"${fn:substring(sampleText, 0, 10)}"</code></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- TAB 3: JSTL XML Demonstration -->
            <div class="tab-pane fade" id="tab-xml" role="tabpanel">
                <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
                    <h5 class="fw-bold mb-3 text-primary"><i class="bi bi-filetype-xml me-2"></i>Topic: JSTL XML Tag Library (Parsing products_catalog.xml)</h5>
                    <p class="text-secondary small">
                        Fulfills syllabus requirement: Demonstrates <code>&lt;x:parse&gt;</code>, <code>&lt;x:out&gt;</code>, and <code>&lt;x:forEach&gt;</code> on an external XML catalog feed.
                    </p>

                    <c:if test="${not empty rawXmlContent}">
                        <x:parse xml="${rawXmlContent}" var="catalogXml" />

                        <div class="table-responsive">
                            <table class="table table-bordered align-middle">
                                <thead class="table-dark small">
                                    <tr>
                                        <th>XML Product ID</th>
                                        <th>Category</th>
                                        <th>Product Name</th>
                                        <th>Brand</th>
                                        <th>Price (₹)</th>
                                        <th>Stock</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <x:forEach select="$catalogXml/catalog/product" var="item">
                                        <tr>
                                            <td><span class="badge bg-light text-dark border">#<x:out select="$item/@id" /></span></td>
                                            <td><span class="badge bg-primary-subtle text-primary"><x:out select="$item/@category" /></span></td>
                                            <td class="fw-bold"><x:out select="$item/name" /></td>
                                            <td><x:out select="$item/brand" /></td>
                                            <td class="text-primary fw-semibold">₹<x:out select="$item/price" /></td>
                                            <td><x:out select="$item/stock" /></td>
                                        </tr>
                                    </x:forEach>
                                </tbody>
                            </table>
                        </div>

                        <details class="mt-3">
                            <summary class="small fw-semibold text-muted cursor-pointer">View Raw Source of products_catalog.xml</summary>
                            <pre class="code-preview-box mt-2"><c:out value="${rawXmlContent}" /></pre>
                        </details>
                    </c:if>
                </div>
            </div>

            <!-- TAB 4: Internationalization (i18n) -->
            <div class="tab-pane fade" id="tab-i18n" role="tabpanel">
                <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
                    <h5 class="fw-bold mb-3 text-primary"><i class="bi bi-globe-americas me-2"></i>Topic: Internationalization (Locale, ResourceBundle, Currency & Date)</h5>
                    <p class="text-secondary small">
                        Dynamically loads localized strings from <code>messages_en.properties</code> or <code>messages_hi.properties</code> according to active session Locale.
                    </p>

                    <div class="row g-4">
                        <div class="col-md-6">
                            <div class="p-3 bg-light rounded-3 border h-100">
                                <h6 class="fw-bold mb-3">ResourceBundle Key-Value Extraction</h6>
                                <div class="table-responsive">
                                    <table class="table table-sm small mb-0">
                                        <thead>
                                            <tr>
                                                <th>Bundle Key</th>
                                                <th>Localized Text (${activeLang})</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr><td><code>app.title</code></td><td class="fw-semibold text-primary">${bundle['app.title']}</td></tr>
                                            <tr><td><code>app.tagline</code></td><td>${bundle['app.tagline']}</td></tr>
                                            <tr><td><code>nav.products</code></td><td>${bundle['nav.products']}</td></tr>
                                            <tr><td><code>cart.checkout</code></td><td>${bundle['cart.checkout']}</td></tr>
                                            <tr><td><code>hero.shop_now</code></td><td>${bundle['hero.shop_now']}</td></tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>

                        <div class="col-md-6">
                            <div class="p-3 bg-light rounded-3 border h-100">
                                <h6 class="fw-bold mb-3">Locale-Specific Formatting</h6>
                                <div class="d-flex flex-column gap-3 small">
                                    <div class="p-2 bg-white rounded border">
                                        <div class="text-muted">Active Java Locale:</div>
                                        <span class="fs-6 fw-bold text-dark">${sessionScope.currentLocale}</span>
                                    </div>
                                    <div class="p-2 bg-white rounded border">
                                        <div class="text-muted">Formatted Currency Value:</div>
                                        <span class="fs-5 fw-bold text-success">${formattedCurrencySample}</span>
                                    </div>
                                    <div class="p-2 bg-white rounded border">
                                        <div class="text-muted">Locale-Formatted Timestamp:</div>
                                        <span class="fw-semibold text-dark">${formattedDate}</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- TAB 5: Sockets & RMI Architecture -->
            <div class="tab-pane fade" id="tab-net" role="tabpanel">
                <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
                    <h5 class="fw-bold mb-3 text-primary"><i class="bi bi-diagram-3-fill me-2"></i>Topic: Distributed Computing: java.net Sockets & Java RMI</h5>

                    <div class="row g-4">
                        <div class="col-md-6">
                            <div class="border rounded-3 p-3 h-100">
                                <div class="d-flex justify-content-between align-items-center mb-2">
                                    <h6 class="fw-bold mb-0">1. java.net Socket Module</h6>
                                    <span class="badge bg-primary">Port 8088</span>
                                </div>
                                <p class="text-muted small">
                                    Multi-threaded <code>ServerSocket</code> accepting remote client commands with Object Serialization (ObjectOutputStream / ObjectInputStream).
                                </p>
                                <ul class="small text-secondary mb-3">
                                    <li>Server: <code>com.shopsphere.network.server.InventoryServer</code></li>
                                    <li>Client: <code>com.shopsphere.network.client.InventoryClient</code></li>
                                    <li>Protocol: <code>GET_ALL, GET_PRODUCT, GET_STOCK, UPDATE_STOCK</code></li>
                                </ul>
                                <div class="alert alert-info py-2 small mb-0">
                                    Run <code>start-network-server.bat</code> and <code>start-network-client.bat</code> to launch live.
                                </div>
                            </div>
                        </div>

                        <div class="col-md-6">
                            <div class="border rounded-3 p-3 h-100">
                                <div class="d-flex justify-content-between align-items-center mb-2">
                                    <h6 class="fw-bold mb-0">2. Java RMI Registry Module</h6>
                                    <span class="badge bg-success">Port 1099</span>
                                </div>
                                <p class="text-muted small">
                                    Remote Interface <code>InventoryService</code> extending <code>java.rmi.Remote</code> bound to RMI Registry using <code>Naming.rebind()</code>.
                                </p>
                                <ul class="small text-secondary mb-3">
                                    <li>Interface: <code>com.shopsphere.rmi.remote.InventoryService</code></li>
                                    <li>Impl: <code>com.shopsphere.rmi.server.InventoryServiceImpl</code></li>
                                    <li>Lookup: <code>rmi://localhost:1099/InventoryService</code></li>
                                </ul>
                                <div class="alert alert-success py-2 small mb-0">
                                    Run <code>start-rmi-server.bat</code> and <code>start-rmi-client.bat</code> to launch live.
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- TAB 6: Applets & JNDI / URLConnection -->
            <div class="tab-pane fade" id="tab-applet" role="tabpanel">
                <div class="card border-0 shadow-sm rounded-4 p-4 mb-4">
                    <h5 class="fw-bold mb-3 text-primary"><i class="bi bi-window-desktop me-2"></i>Topic: Java Applets & java.net URLConnection</h5>

                    <div class="row g-4">
                        <div class="col-md-6">
                            <div class="border rounded-3 p-3 h-100">
                                <h6 class="fw-bold mb-2">1. Applet Lifecycle Simulation</h6>
                                <p class="text-secondary small">
                                    Applets used 5 core lifecycle methods in client browsers before industry migrated to server-side Jakarta EE:
                                </p>
                                <div class="d-flex flex-column gap-2 small">
                                    <div class="p-2 bg-light rounded"><code>init()</code> - Invoked once on classloader download</div>
                                    <div class="p-2 bg-light rounded"><code>start()</code> - Invoked when HTML page becomes active</div>
                                    <div class="p-2 bg-light rounded"><code>paint(Graphics g)</code> - Screen redraw callback</div>
                                    <div class="p-2 bg-light rounded"><code>stop()</code> - Suspends active background execution</div>
                                    <div class="p-2 bg-light rounded"><code>destroy()</code> - De-allocates native OS handles</div>
                                </div>
                            </div>
                        </div>

                        <div class="col-md-6">
                            <div class="border rounded-3 p-3 h-100">
                                <h6 class="fw-bold mb-2">2. java.net URLConnection Live Inspection</h6>
                                <p class="text-secondary small">
                                    Demonstrating HTTP Protocol and Content Handlers via standard <code>java.net.URLConnection</code>:
                                </p>
                                <div class="p-3 bg-dark text-white rounded-3 font-monospace small">
                                    <div>Protocol: ${urlDemoInfo.protocol}</div>
                                    <div>Host:     ${urlDemoInfo.host}</div>
                                    <div>Type:     ${urlDemoInfo.contentType}</div>
                                    <div class="text-success mt-2">Status: ${urlDemoInfo.status}</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </div>

    </div>
</main>

<%@ include file="fragments/footer.jsp" %>
